package com.backend.account.service;

import com.backend.account.entity.Account;
import com.backend.account.entity.WithdrawalMembership;
import com.backend.account.repository.AccountCustomRepository;
import com.backend.account.repository.AccountRepository;
import com.backend.account.repository.WithdrawalMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountCustomRepository accountCustomRepository;
    private final AccountRepository accountJpaRepository;
    private final WithdrawalMembershipRepository withdrawalRepository;

    @Override
    public void createAccount(String email, String loginType) {
        accountCustomRepository.save(email, loginType);
    }

    @Override
    public void createGuestAccount(String email, String loginType) {
        createAccount(email, loginType); // 동일 로직 위임
    }

    @Override
    public void createAdminAccount(String email, String loginType) {
        accountCustomRepository.saveAdmin(email, loginType);
    }

    @Override
    public boolean checkEmailDuplication(String email) {
        return accountJpaRepository.existsByEmail(email);
    }

    @Override
    public String findEmail(Long accountId) {
        return accountJpaRepository.findById(accountId)
                .map(Account::getEmail)
                .orElse(null);
    }

    @Override
    public void createWithdrawalAccount(Long accountId) {
        WithdrawalMembership membership = new WithdrawalMembership(accountId, null, null);
        withdrawalRepository.save(membership);
    }

    @Override
    public void createWithdrawAt(Long accountId, LocalDateTime time) {
        updateWithdrawInfo(accountId, time, false);
    }

    @Override
    public void createWithdrawEnd(Long accountId, LocalDateTime time) {
        updateWithdrawInfo(accountId, time, true);
    }

    private void updateWithdrawInfo(Long accountId, LocalDateTime time, boolean isEnd) {
        withdrawalRepository.findByAccountId(accountId)
                .ifPresent(wm -> {
                    if (isEnd) {
                        wm.setWithdrawEnd(time.plusYears(3));
                    } else {
                        wm.setWithdrawAt(time);
                    }
                    // JPA dirty checking
                });
    }

    @Override
    public boolean withdraw(Long accountId) {
        return accountCustomRepository.deleteAccount(accountId);
    }

    @Override
    public long countEmail(String guestEmail) {
        return accountJpaRepository.countByEmailStartingWith(guestEmail);
    }
}