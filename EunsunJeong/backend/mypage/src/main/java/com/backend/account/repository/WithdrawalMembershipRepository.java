package com.backend.account.repository;

import com.backend.account.entity.WithdrawalMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WithdrawalMembershipRepository extends JpaRepository<WithdrawalMembership, Long> {
    Optional<WithdrawalMembership> findByAccountId(String accountId);
}
