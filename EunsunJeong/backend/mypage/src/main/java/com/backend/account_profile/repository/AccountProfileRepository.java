package com.backend.account_profile.repository;

import com.backend.account.entity.Account;
import com.backend.account_profile.entity.AccountProfile;
import com.backend.account_profile.entity.AdminProfile;

import java.util.Optional;

public interface AccountProfileRepository {

    AccountProfile save(Account account, String nickname, String gender, String birthyear, String ageRange);

    AdminProfile saveAdmin(Account account, String email);

    Optional<AccountProfile> findByAccount(Account account);

    Optional<String> findEmail(Long accountId);

    Optional<String> findRoleType(Long accountId);

    Optional<String> findNickname(Long accountId);

    Optional<String> findGender(Long accountId);

    Optional<String> findBirthyear(Long accountId);
}