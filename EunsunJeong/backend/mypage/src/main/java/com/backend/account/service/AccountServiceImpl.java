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
        createAccount(email, loginType);
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
    public void createWithdrawAccount(String accountId) {
        WithdrawalMembership membership = new WithdrawalMembership(accountId, null, null);
        withdrawalRepository.save(membership);
    }

    @Override
    public void createWithdrawAt(String accountId, LocalDateTime time) {
        withdrawalRepository.findByAccountId(accountId)
                .ifPresent(wm -> {
                    wm.setWithdrawAt(time);
                    withdrawalRepository.save(wm);
                });
    }

    @Override
    public void createWithdrawEnd(String accountId, LocalDateTime time) {
        withdrawalRepository.findByAccountId(accountId)
                .ifPresent(wm -> {
                    wm.setWithdrawEnd(time.plusYears(3));
                    withdrawalRepository.save(wm);
                });
    }

    @Override
    public boolean withdraw(String accountIdStr) {
        return accountCustomRepository.deleteAccount(accountIdStr);
    }

    //게스트 계정 자동 생성 시 사용
    /*
    guest_1@example.com
    guest_2@example.com
    ...
    guest_N@example.com
     */
    @Override
    public long countEmail(String guestEmailPrefix) {
        return accountJpaRepository.countByEmailStartingWith(guestEmailPrefix);
    }
}
