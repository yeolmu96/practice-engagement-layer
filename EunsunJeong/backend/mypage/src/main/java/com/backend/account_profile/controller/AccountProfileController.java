package com.backend.account_profile.controller;

import com.backend.account_profile.controller.response_form.AccountProfileResponseForm;
import com.backend.account_profile.service.AccountProfileService;
import com.backend.account_profile.service.TokenAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account-profile")
public class AccountProfileController {

    private final AccountProfileService accountProfileService;
    private final TokenAccountService tokenAccountService;

    @PostMapping("/info")
    public ResponseEntity<?> requestInfo(@RequestBody Map<String, String> request) {
        try {
            String userToken = request.get("userToken");
            Long accountId = tokenAccountService.resolveAccountId(userToken);

            log.info("AccountProfile 조회 요청: accountId={}", accountId);

            AccountProfileResponseForm response = AccountProfileResponseForm.builder()
                    .email(accountProfileService.findEmail(accountId))
                    .nickname(accountProfileService.findNickname(accountId))
                    .gender(accountProfileService.findGender(accountId))
                    .birthyear(accountProfileService.findBirthyear(accountId))
                    .build();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("AccountProfile 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "서버 오류가 발생했습니다"));
        }
    }
}