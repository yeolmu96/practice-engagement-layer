package com.backend.mypage.controller;

import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MyPageService myPageService;

    //마이페이지 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrGetMypage(@RequestHeader("Authorization") String token) {
        log.info("받은 토큰: {}", token);
        MyPageProfile profile = myPageService.initializeMypage(token.replace("Bearer ", ""));
        return ResponseEntity.ok(profile);
    }
}
