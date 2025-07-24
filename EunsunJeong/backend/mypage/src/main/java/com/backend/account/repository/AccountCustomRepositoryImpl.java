package com.backend.account.repository;

import com.backend.account.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(String email, String loginTypeStr) {
        saveWithRole(email, loginTypeStr, RoleType.NORMAL);
    }

    @Override
    public void saveAdmin(String email, String loginTypeStr) {
        saveWithRole(email, loginTypeStr, RoleType.ADMIN);
    }

    private void saveWithRole(String email, String loginTypeStr, RoleType roleTypeEnum) {
        LoginType loginType = LoginType.valueOf(loginTypeStr);

        AccountRoleType roleType = findOrCreateRoleType(roleTypeEnum);
        AccountLoginType loginTypeEntity = findOrCreateLoginType(loginType);

        Account account = new Account(email, roleType, loginTypeEntity);
        em.persist(account);
    }

    private AccountRoleType findOrCreateRoleType(RoleType roleTypeEnum) {
        return em.createQuery(
                        "SELECT r FROM AccountRoleType r WHERE r.roleType = :role", AccountRoleType.class)
                .setParameter("role", roleTypeEnum)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountRoleType newRole = new AccountRoleType(roleTypeEnum);
                    em.persist(newRole);
                    return newRole;
                });
    }

    private AccountLoginType findOrCreateLoginType(LoginType loginType) {
        return em.createQuery(
                        "SELECT l FROM AccountLoginType l WHERE l.loginType = :login", AccountLoginType.class)
                .setParameter("login", loginType)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    AccountLoginType newLogin = new AccountLoginType(loginType);
                    em.persist(newLogin);
                    return newLogin;
                });
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
}
