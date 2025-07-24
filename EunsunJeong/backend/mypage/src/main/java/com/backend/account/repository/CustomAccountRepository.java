package com.backend.account.repository;

import com.backend.account.entity.Account;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomAccountRepository {

    void save(String email, String loginType);

    void saveAdmin(String email, String loginType);

    void saveWithdralInfo(String accountId);

    Optional<Account> findById(Long accountId);

    Optional<Account> findByEmail(String email);

    void saveWithdrawAt(Long accountId, LocalDateTime time);

    void saveWithdrawEnd(Long accountId, LocalDateTime time);

    boolean deleteAccount(Long accountId);

    long countByEmail(String guestEmail);
}
