package com.backend.account_profile.service.request;

import lombok.Getter;

@Getter
public class AccountProfileRequest {
    private String nickname;
    private String gender;
    private String birthyear;
    private String ageRange;
}
