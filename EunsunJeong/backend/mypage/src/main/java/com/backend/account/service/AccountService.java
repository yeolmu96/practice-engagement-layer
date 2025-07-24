package com.backend.account.service;

import java.time.LocalDateTime;

public interface AccountService {

    void createAccount(String email, String loginType);
    void createAdminAccount(String email, String loginType);
    void createWithdrawalAccount(String accountId);
    boolean checkEmailDuplication(String email);
    String findEmail(Long accountId);
    void createWithdrawAt(Long accountId, LocalDateTime time);
    void createWithdrawEnd(Long accountId, LocalDateTime time);
    boolean withdraw(Long accountId);
    void createGuestAccount(String guestEmail, String loginType);
    long countEmail(String guestEmail);

}
