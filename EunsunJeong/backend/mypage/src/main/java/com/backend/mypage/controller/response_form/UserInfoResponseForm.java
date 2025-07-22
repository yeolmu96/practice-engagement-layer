package com.backend.mypage.controller.response_form;

public class UserInfoResponseForm {

    private String email;
    private boolean success;

    //Getter, Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
