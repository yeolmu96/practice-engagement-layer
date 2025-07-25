package com.backend.account_profile.repository;

import com.backend.account.entity.Account;
import com.backend.account_profile.entity.AccountProfile;
import com.backend.account_profile.entity.AdminProfile;

import java.util.Optional;

public interface AccountProfileRepository {

    /**
     * AccountProfile 저장
     */
    AccountProfile save(Account account, String nickname, String gender, String birthyear, String ageRange);

    /**
     * AdminProfile 저장
     */
    AdminProfile saveAdmin(Account account, String email);

    /**
     * Account로 Profile 조회
     */
    Optional<AccountProfile> findByAccount(Account account);

    /**
     * accountId로 Profile 조회 (재사용용)
     */
    Optional<AccountProfile> findByAccountId(Long accountId);

    /**
     * 닉네임, 성별, 생년, 역할, 이메일 등 개별 조회
     */
    Optional<String> findNickname(Long accountId);
    Optional<String> findGender(Long accountId);
    Optional<String> findBirthyear(Long accountId);
    Optional<String> findRoleType(Long accountId);
    Optional<String> findEmail(Long accountId);
}
