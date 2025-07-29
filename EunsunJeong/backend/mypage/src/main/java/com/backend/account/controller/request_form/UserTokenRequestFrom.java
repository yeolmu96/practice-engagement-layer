package com.backend.account.controller.request_form;

public class UserTokenRequestFrom {
    private String userToken;

    public UserTokenRequestFrom(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}