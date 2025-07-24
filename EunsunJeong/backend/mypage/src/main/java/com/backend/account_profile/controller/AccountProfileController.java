package com.backend.account_profile.controller;

import com.backend.account_profile.controller.response_form.AccountProfileResponseForm;
import com.backend.account_profile.service.AccountProfileService;
import com.backend.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account-profile")
public class AccountProfileController {

    private final AccountProfileService accountProfileService;
    private final RedisCacheService redisCacheService;

    @PostMapping("/info")
    public ResponseEntity<?> requestInfo(@RequestBody Map<String, String> request) {
        String userToken = request.get("userToken");
        System.out.println("인포용 userToken: " + userToken);

        if (userToken == null || userToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "userToken이 필요합니다"));
        }

        try {
            String accountId = redisCacheService.getValueByKey(userToken, String.class);
            if (accountId == null) {
                return ResponseEntity.status(404).body(Map.of("error", "유효하지 않은 userToken입니다"));
            }

            AccountProfileResponseForm response = AccountProfileResponseForm.builder()
                    .email(accountProfileService.findEmail(accountId))
                    .nickname(accountProfileService.findNickname(accountId))
                    .gender(accountProfileService.findGender(accountId))
                    .birthyear(accountProfileService.findBirthyear(accountId))
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
