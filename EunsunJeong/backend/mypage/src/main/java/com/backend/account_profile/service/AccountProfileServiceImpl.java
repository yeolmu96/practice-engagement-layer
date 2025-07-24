package com.backend.account_profile.service;

import com.backend.account.entity.Account;
import com.backend.account.repository.AccountCustomRepository;
import com.backend.account_profile.entity.AccountProfile;
import com.backend.account_profile.entity.AdminProfile;
import com.backend.account_profile.repository.AccountProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountProfileServiceImpl implements AccountProfileService {

    private final AccountProfileRepository accountProfileRepository;
    private final AccountCustomRepository accountRepository;

    @Override
    public boolean createAccountProfile(Long accountId, String nickname, String gender, String birthyear, String ageRange) {
        System.out.println("profile 진입");
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            System.out.println("Account 없음");
            return false;
        }
        Account account = optionalAccount.get();

        String originalNickname = (nickname == null || nickname.isBlank()) ? "temporary" : nickname;
        String newNickname = originalNickname;
        int count = 1;

        while (true) {
            try {
                accountProfileRepository.save(account, newNickname, gender, birthyear, ageRange);
                break; // 저장 성공
            } catch (Exception e) {
                newNickname = originalNickname + "_" + count++;
                System.out.println("닉네임 중복 발생, 시도: " + newNickname);
            }
        }

        System.out.println("Profile 생성 성공");
        return true;
    }

    @Override
    public boolean createAdminProfile(Long accountId, String email) {
        System.out.println("adminProfile 진입");

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            System.out.println("Account 없음");
            return false;
        }

        AdminProfile saved = accountProfileRepository.saveAdmin(optionalAccount.get(), email);
        if (saved != null) {
            System.out.println("AdminProfile 생성 성공: " + saved);
            return true;
        }

        return false;
    }

    @Override
    public String findEmail(Long accountId) {
        return accountProfileRepository.findEmail(accountId).orElse(null);
    }

    @Override
    public String findRoleType(Long accountId) {
        return accountProfileRepository.findRoleType(accountId).orElse(null);
    }

    @Override
    public String findNickname(Long accountId) {
        return accountProfileRepository.findNickname(accountId).orElse(null);
    }

    @Override
    public String findGender(Long accountId) {
        return accountProfileRepository.findGender(accountId).orElse(null);
    }

    @Override
    public String findBirthyear(Long accountId) {
        return accountProfileRepository.findBirthyear(accountId).orElse(null);
    }

    @Override
    public boolean updateAccountProfileIfExists(Long accountId, String nickname, String gender, String birthyear, String ageRange) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            return false;
        }

        Account account = optionalAccount.get();
        Optional<AccountProfile> optionalProfile = accountProfileRepository.findByAccount(account);

        if (optionalProfile.isPresent()) {
            AccountProfile profile = optionalProfile.get();

            if (nickname != null) profile.setNickname(nickname);
            if (gender != null) profile.setGender(gender);
            if (birthyear != null) profile.setBirthyear(birthyear);
            if (ageRange != null) profile.setAgeRange(ageRange);

            return true;
        } else {
            return createAccountProfile(accountId, nickname, gender, birthyear, ageRange);
        }
    }
}
