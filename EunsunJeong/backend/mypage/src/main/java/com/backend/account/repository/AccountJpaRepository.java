package com.backend.account.repository;

import com.backend.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    long countByEmailStartingWith(String prefix);
}
