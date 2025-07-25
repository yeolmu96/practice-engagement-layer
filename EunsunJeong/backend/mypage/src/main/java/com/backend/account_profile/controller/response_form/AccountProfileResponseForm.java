package com.backend.account_profile.controller.response_form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountProfileResponseForm {
    private String email;
    private String nickname;
    private String gender;
    private String birthyear; // ← String으로 통일
}