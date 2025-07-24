package com.backend.account.repository;

import com.backend.account.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(String email, String loginTypeStr) {
        RoleType defaultRole = RoleType.NORMAL;
        LoginType loginType = LoginType.valueOf(loginTypeStr);

        // RoleType 엔티티 확인 또는 생성
        AccountRoleType roleType = em.createQuery(
                        "SELECT r FROM AccountRoleType r WHERE r.roleType = :role", AccountRoleType.class)
                .setParameter("role", defaultRole)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountRoleType newRole = new AccountRoleType(defaultRole);
                    em.persist(newRole);
                    return newRole;
                });

        // LoginType 엔티티 확인 또는 생성
        AccountLoginType loginTypeEntity = em.createQuery(
                        "SELECT l FROM AccountLoginType l WHERE l.loginType = :login", AccountLoginType.class)
                .setParameter("login", loginType)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountLoginType newLogin = new AccountLoginType(loginType);
                    em.persist(newLogin);
                    return newLogin;
                });

        Account account = new Account(email, roleType, loginTypeEntity);
        em.persist(account);
    }

    @Override
    public void saveAdmin(String email, String loginTypeStr) {
        saveWithRole(email, loginTypeStr, RoleType.ADMIN);
    }

    private void saveWithRole(String email, String loginTypeStr, RoleType roleTypeEnum) {
        LoginType loginType = LoginType.valueOf(loginTypeStr);

        AccountRoleType roleType = em.createQuery(
                        "SELECT r FROM AccountRoleType r WHERE r.roleType = :role", AccountRoleType.class)
                .setParameter("role", roleTypeEnum)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountRoleType newRole = new AccountRoleType(roleTypeEnum);
                    em.persist(newRole);
                    return newRole;
                });

        AccountLoginType loginTypeEntity = em.createQuery(
                        "SELECT l FROM AccountLoginType l WHERE l.loginType = :login", AccountLoginType.class)
                .setParameter("login", loginType)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountLoginType newLogin = new AccountLoginType(loginType);
                    em.persist(newLogin);
                    return newLogin;
                });

        Account account = new Account(email, roleType, loginTypeEntity);
        em.persist(account);
    }

    @Override
    public void saveWithdralInfo(String accountId) {
        WithdrawalMembership wm = new WithdrawalMembership(accountId, null, null);
        em.persist(wm);
    }

    @Override
    public Optional<Account> findById(Long accountId) {
        return Optional.ofNullable(em.find(Account.class, accountId));
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return em.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void saveWithdrawAt(Long accountId, LocalDateTime time) {
        WithdrawalMembership wm = getMembershipByAccountId(accountId.toString());
        if (wm != null) {
            wm.setWithdrawAt(time);
            em.merge(wm);
        }
    }

    @Override
    public void saveWithdrawEnd(Long accountId, LocalDateTime time) {
        WithdrawalMembership wm = getMembershipByAccountId(accountId.toString());
        if (wm != null) {
            wm.setWithdrawEnd(time.plusYears(3));
            em.merge(wm);
        }
    }

    private WithdrawalMembership getMembershipByAccountId(String accountId) {
        return em.createQuery(
                        "SELECT w FROM WithdrawalMembership w WHERE w.accountId = :accountId", WithdrawalMembership.class)
                .setParameter("accountId", accountId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        Account account = em.find(Account.class, accountId);
        if (account != null) {
            em.remove(account);
            return true;
        }
        return false;
    }

    @Override
    public long countByEmail(String guestEmail) {
        return em.createQuery("SELECT COUNT(a) FROM Account a WHERE a.email LIKE :prefix", Long.class)
                .setParameter("prefix", guestEmail + "%")
                .getSingleResult();
    }
}
