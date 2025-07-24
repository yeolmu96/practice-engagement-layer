package com.backend.account_profile.service;

public interface AccountProfileService {

    boolean createAccountProfile(Long accountId, String nickname, String gender, String birthyear, String ageRange);

    boolean createAdminProfile(Long accountId, String email);

    String findEmail(Long accountId);

    String findRoleType(Long accountId);

    String findNickname(Long accountId);

    String findGender(Long accountId);

    String findBirthyear(Long accountId);

    boolean updateAccountProfileIfExists(Long accountId, String nickname, String gender, String birthyear, String ageRange);
}
