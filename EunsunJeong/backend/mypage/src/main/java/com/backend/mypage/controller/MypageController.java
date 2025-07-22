package com.backend.mypage.controller;

import com.backend.mypage.controller.request_form.MyPageProfileRequest;
import com.backend.mypage.controller.response_form.MyPageProfileResponse;
import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.service.MyPageService;
import com.backend.mypage.service.UserInfoRequestService;
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
    private final UserInfoRequestService userInfoRequestService;

    //마이페이지 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrGetMypage(@RequestHeader("Authorization") String token) {
        MyPageProfile profile = myPageService.initializeMypage(token.replace("Bearer ", ""));
        return ResponseEntity.ok(profile);
    }

    //마이페이지 조회
    @GetMapping("/get")
    public ResponseEntity<MyPageProfileResponse> getMyPage(@RequestHeader("Authorization") String token){
        String email = extractEmailFromToken(token);
        return ResponseEntity.ok(myPageService.getMyPageByEmail(email));
    }

    //마이페이지 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateMyPage(@RequestHeader("Authorization") String token,
                                               @RequestBody MyPageProfileRequest request){
        String email = extractEmailFromToken(token);
        myPageService.updateMyPage(email, request);
        return ResponseEntity.ok("수정 완료");
    }

    //마이페이지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyPage(@RequestHeader("Authorization") String token){
        String email = extractEmailFromToken(token);
        myPageService.deleteMyPage(email);
        return ResponseEntity.ok("삭제 완료");
    }

    //token에서 email 추출 유틸
    private String extractEmailFromToken(String token){
        return userInfoRequestService.getEmailFromUserToken(token.replace("Bearer ", ""));
    }
}