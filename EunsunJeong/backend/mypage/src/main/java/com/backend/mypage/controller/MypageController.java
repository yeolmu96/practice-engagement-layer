package com.backend.mypage.controller;

import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.entitiy.MyPageUser;
import com.backend.mypage.repository.MyPageRepository;
import com.backend.mypage.repository.MyPageUserRepository;
import com.backend.mypage.service.UserInfoRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final UserInfoRequestService userInfoRequestService;
    private final MyPageUserRepository myPageUserRepository;
    private final MyPageRepository myPageRepository;

    //마이페이지 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrGetMypage(@RequestBody Map<String, String> payload) {

        String userToken = payload.get("userToken");
        String email = userInfoRequestService.getEmailFromUserToken(userToken);

        if(email == null){
            return ResponseEntity.status(401).body("Invalid user token");
        }

        //Django에서 가입한 유저 정보를 바탕으로, Spring 시스템에 필요한 유저 레코드를 생성
        MyPageUser user = myPageUserRepository.findByEmail(email)
                //회원가입 아니고 마이페이지 관리를 위한 초기화 단계
                .orElseGet(() -> myPageUserRepository.save(new MyPageUser(email)));

        //마이페이지 조회 또는 생성
        MyPageProfile mypage = myPageRepository.findByUser(user)
                .orElseGet(() -> {
                    MyPageProfile newMypage = new MyPageProfile();
                    newMypage.setUser(user);
                    return myPageRepository.save(newMypage);
                });

        return ResponseEntity.ok(mypage);
    }
}
