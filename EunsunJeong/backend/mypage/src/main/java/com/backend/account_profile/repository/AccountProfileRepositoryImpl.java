package com.backend.account_profile.repository;

import com.backend.account.entity.Account;
import com.backend.account_profile.entity.AccountProfile;
import com.backend.account_profile.entity.AdminProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountProfileRepositoryImpl implements AccountProfileRepository {

    private final AccountProfileJpaRepository accountProfileJpaRepository;
    private final AdminProfileJpaRepository adminProfileJpaRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * AccountProfile 저장
     */
    @Transactional
    public AccountProfile save(Account account, String nickname, String gender, String birthyear, String ageRange) {
        System.out.println("AccountProfile 저장 시도: " + gender + ", " + birthyear + ", " + ageRange);

        gender = (gender == null || gender.isBlank()) ? "None" : gender;
        birthyear = (birthyear == null || birthyear.isBlank()) ? "0000" : birthyear;
        ageRange = (ageRange == null || ageRange.isBlank()) ? "None" : ageRange;

        String originalNickname = (nickname == null || nickname.isBlank()) ? "temporary" : nickname;
        String newNickname = originalNickname;
        int count = 1;

        while (accountProfileJpaRepository.existsByNickname(newNickname)) {
            newNickname = originalNickname + "_" + count++;
            System.out.println("Nickname 중복, 새 시도: " + newNickname);
        }

        AccountProfile profile = AccountProfile.builder()
                .account(account)
                .nickname(newNickname)
                .gender(gender)
                .birthyear(birthyear)
                .ageRange(ageRange)
                .build();

        return accountProfileJpaRepository.save(profile);
    }

    /**
     * AdminProfile 저장
     */
    @Transactional
    public AdminProfile saveAdmin(Account account, String email) {
        AdminProfile adminProfile = AdminProfile.builder()
                .account(account)
                .email(email)
                .build();

        return adminProfileJpaRepository.save(adminProfile);
    }

    public Optional<AccountProfile> findByAccount(Account account) {
        return accountProfileJpaRepository.findByAccount(account);
    }

    public Optional<String> findEmail(Long accountId) {
        return accountProfileJpaRepository.findByAccount_Id(accountId)
                .map(profile -> profile.getAccount().getEmail());
    }

    public Optional<String> findRoleType(Long accountId) {
        return accountProfileJpaRepository.findByAccount_Id(accountId)
                .map(profile -> profile.getAccount().getRoleType().name()); // or getRoleTypeId() if Integer
    }

    public Optional<String> findNickname(Long accountId) {
        return accountProfileJpaRepository.findByAccount_Id(accountId)
                .map(AccountProfile::getNickname);
    }

    public Optional<String> findGender(Long accountId) {
        return accountProfileJpaRepository.findByAccount_Id(accountId)
                .map(AccountProfile::getGender);
    }

    public Optional<String> findBirthyear(Long accountId) {
        return accountProfileJpaRepository.findByAccount_Id(accountId)
                .map(AccountProfile::getBirthyear);
    }
}