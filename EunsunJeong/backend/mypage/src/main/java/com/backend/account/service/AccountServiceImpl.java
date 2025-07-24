package com.backend.account.service;

import com.backend.account.entity.Account;
import com.backend.account.repository.CustomAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final CustomAccountRepository accountRepository;

    @Override
    public void createAccount(String email, String loginType) {
        accountRepository.save(email, loginType);
    }

    @Override
    public void createAdminAccount(String email, String loginType) {
        accountRepository.saveAdmin(email, loginType);
    }

    @Override
    public void createWithdrawalAccount(String accountId) {
        accountRepository.saveWithdralInfo(accountId);
    }

    @Override
    public boolean checkEmailDuplication(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Override
    public String findEmail(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getEmail)
                .orElse(null);
    }

    @Override
    public void createWithdrawAt(Long accountId, LocalDateTime time) {
        accountRepository.saveWithdrawAt(accountId, time);
    }

    @Override
    public void createWithdrawEnd(Long accountId, LocalDateTime time) {
        accountRepository.saveWithdrawEnd(accountId, time);
    }

    @Override
    public boolean withdraw(Long accountId) {
        return accountRepository.deleteAccount(accountId);
    }

    @Override
    public void createGuestAccount(String guestEmail, String loginType) {
        accountRepository.save(guestEmail, loginType);
    }

    @Override
    public long countEmail(String guestEmail) {
        return accountRepository.countByEmail(guestEmail);
    }
}
