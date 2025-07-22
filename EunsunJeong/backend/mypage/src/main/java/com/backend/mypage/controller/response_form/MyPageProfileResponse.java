package com.backend.mypage.controller.response_form;

import com.backend.mypage.entitiy.MyPageProfile;

public class MyPageProfileResponse {

    private String someInfo;

    public static MyPageProfileResponse from(MyPageProfile profile) {
        MyPageProfileResponse response = new MyPageProfileResponse();
        response.someInfo = profile.getSomeInfo();
        return response;
    }

    public String getSomeInfo() {
        return someInfo;
    }

    public void setSomeInfo(String someInfo) {
        this.someInfo = someInfo;
    }
}
