package com.backend.account_profile.repository;

import com.backend.account.entity.Account;
import com.backend.account_profile.entity.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountProfileJpaRepository extends JpaRepository<AccountProfile, Long> {
    Optional<AccountProfile> findByAccount(Account account);
    Optional<AccountProfile> findByAccount_Id(Long accountId); // accountId가 String이라면
    boolean existsByNickname(String nickname);
}