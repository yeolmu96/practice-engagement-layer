package com.backend.account.controller;

import com.backend.account.controller.request_form.UserTokenRequestFrom;
import com.backend.account.service.AccountService;
import com.backend.userdashboard.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final RedisCacheService redisCacheService;

    @PostMapping("/email")
    public ResponseEntity<?> requestEmail(@RequestBody UserTokenRequestFrom request) {
        String userToken = request.getUserToken();

        if (userToken == null || userToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "userToken이 필요합니다",
                    "success", false
            ));
        }

        try {
            String accountId = redisCacheService.getValueByKey(userToken, String.class);
            if (accountId == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "error", "유효한 userToken이 아닙니다",
                        "success", false
                ));
            }

            String email = accountService.findEmail(Long.parseLong(accountId));
            if (email == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "error", "이메일을 찾을 수 없습니다",
                        "success", false
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "success", true
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "서버 내부 오류",
                    "success", false
            ));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> requestWithdraw(@RequestBody UserTokenRequestFrom request) {
        String userToken = request.getUserToken();

        if (userToken == null || userToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "userToken이 필요합니다",
                    "success", false
            ));
        }

        try {
            String accountId = redisCacheService.getValueByKey(userToken, String.class);
            if (accountId == null) {
                return ResponseEntity.status(404).body(Map.of(
                        "error", "유효한 userToken이 아닙니다",
                        "success", false
                ));
            }

            accountService.createWithdrawalAccount(accountId);

            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            accountService.createWithdrawAt(Long.parseLong(accountId), now);
            accountService.createWithdrawEnd(Long.parseLong(accountId), now);

            boolean success = accountService.withdraw(Long.parseLong(accountId));
            if (!success) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "회원 탈퇴에 실패했습니다",
                        "success", false
                ));
            }

            redisCacheService.deleteByKey(userToken);
            redisCacheService.deleteByKey(accountId);

            Map<String, Object> response = new HashMap<>();
            response.put("accountId", accountId);
            response.put("withdraw_at", now);
            response.put("withdraw_end", now.plusYears(3));
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "서버 내부 오류",
                    "success", false
            ));
        }
    }
}