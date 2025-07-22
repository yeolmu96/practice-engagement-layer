package com.backend.mypage.service;

import com.backend.mypage.controller.response_form.UserInfoResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/*
외부 시스템(django) 호출 담당
 */

@Service
@RequiredArgsConstructor
public class UserInfoRequestService {

    @Value("${external.django.base-url}")
    private String djangoBaseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    //userToken으로 email 가져오기
    public String getEmailFromUserToken(String userToken){

        //테스트 우회용
        if("test".equals(userToken)){
            return "test@example.com";
        }

        //실제 서비스용
        //Django API(email 응답)
        String url = djangoBaseUrl + "/account/request-email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("userToken", userToken);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<UserInfoResponseForm> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    UserInfoResponseForm.class
            );

            if(response.getBody() != null && response.getBody().isSuccess()) {
                return response.getBody().getEmail();
            }

        } catch(Exception e) {
                System.out.println("Django base url error: " + e.getMessage());
        }

        return null;
    }
}
