package com.backend.account.repository;

import com.backend.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountCustomRepository {
    boolean existsByEmail(String email);
    long countByEmailStartingWith(String prefix);
}
