package com.backend.account.controller;

import com.backend.account.controller.request_form.UserTokenRequestFrom;
import com.backend.account.service.AccountService;
import com.backend.redis_cache.RedisCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private RedisCacheService redisCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("/account/email - 이메일 반환 성공")
    void requestEmail_success() throws Exception {
        //given
        String token = "mock_token";
        Long accountId = 1L;
        String email = "user@example.com";

        Mockito.when(redisCacheService.getValueByKey(eq(token), eq(Long.class)))
                .thenReturn(accountId);
        Mockito.when(accountService.findEmail(accountId))
                .thenReturn(email);

        UserTokenRequestFrom request = new UserTokenRequestFrom(token);

        //when, then
        mockMvc.perform(post("/account/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @DisplayName("/account/email() - 이메일 없음")
    void requestEmail_fail() throws Exception {
        //given
        String token = "mock_token";
        Long accountId = 1L;

        Mockito.when(redisCacheService.getValueByKey(eq(token), eq(Long.class)))
                .thenReturn(accountId);
        Mockito.when(accountService.findEmail(accountId))
                .thenReturn(null);

        UserTokenRequestFrom request = new UserTokenRequestFrom(token);

        //when + then
        mockMvc.perform(post("/account/email")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("이메일을 찾을 수 없습니다"));
    }

    @Test
    @DisplayName("/account/email - userToken이 비어있음")
    void requestEmail_blankToken() throws Exception {
        UserTokenRequestFrom request = new UserTokenRequestFrom("");

        mockMvc.perform(post("/account/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("userToken이 필요합니다"));
    }

    @Test
    @DisplayName("/account/withdraw - 탈퇴 처리 성공")
    void requestWithdraw_success() throws Exception {
        //given
        String token = "mock_token";
        Long accountId = 1L;
        String accountIdStr = "1";

        Mockito.when(redisCacheService.getValueByKey(eq(token), eq(Long.class)))
                .thenReturn(accountId);

        Mockito.doNothing().when(accountService).createWithdrawAccount(accountIdStr);
        Mockito.doNothing().when(accountService).createWithdrawAt(eq(accountIdStr), any(LocalDateTime.class));
        Mockito.doNothing().when(accountService).createWithdrawEnd(eq(accountIdStr), any(LocalDateTime.class));
        Mockito.when(accountService.withdraw(accountIdStr)).thenReturn(true);
        Mockito.doNothing().when(redisCacheService).deleteByKey(anyString());

        UserTokenRequestFrom request = new UserTokenRequestFrom(token);

        //when + then
        mockMvc.perform(post("/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.accountId").value(accountId));
    }

    @Test
    @DisplayName("/account/withdraw - withdraw() 실패 반환")
    void requestWithdraw_fail() throws Exception {
        String token = "mock_token";
        Long accountId = 1L;
        String accountIdStr = "1";

        Mockito.when(redisCacheService.getValueByKey(eq(token), eq(Long.class)))
                .thenReturn(accountId);

        Mockito.doNothing().when(accountService).createWithdrawAccount(accountIdStr);
        Mockito.doNothing().when(accountService).createWithdrawAt(eq(accountIdStr), any(LocalDateTime.class));
        Mockito.doNothing().when(accountService).createWithdrawEnd(eq(accountIdStr), any(LocalDateTime.class));
        Mockito.when(accountService.withdraw(accountIdStr)).thenReturn(false);

        UserTokenRequestFrom request = new UserTokenRequestFrom(token);

        mockMvc.perform(post("/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("회원 탈퇴에 실패했습니다"));
    }

    @Test
    @DisplayName("/account/withdraw - userToken 유효하지 않음")
    void requestWithdraw_invalidToken() throws Exception {
        String token = "invalid_token";

        Mockito.when(redisCacheService.getValueByKey(eq(token), eq(Long.class)))
                .thenReturn(null);

        UserTokenRequestFrom request = new UserTokenRequestFrom(token);

        mockMvc.perform(post("/account/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("유효한 userToken이 아닙니다"));
    }
}