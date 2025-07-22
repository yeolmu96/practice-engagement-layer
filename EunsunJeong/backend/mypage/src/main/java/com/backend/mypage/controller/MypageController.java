package com.backend.mypage.controller;

import com.backend.mypage.controller.request_form.MyPageProfileRequest;
import com.backend.mypage.controller.response_form.MyPageProfileResponse;
import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.entitiy.MyPageUser;
import com.backend.mypage.repository.MyPageProfileRepository;
import com.backend.mypage.repository.MyPageUserRepository;
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
    private final MyPageProfileRepository myPageProfileRepository;
    private final MyPageUserRepository myPageUserRepository;
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

        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        return ResponseEntity.ok(MyPageProfileResponse.from(profile));
    }

    //마이페이지 수정
    @PutMapping("/edit")
    public ResponseEntity<String> updateMyPage(@RequestHeader("Authorization") String token,
                                               @RequestBody MyPageProfileRequest request){
        String email = extractEmailFromToken(token);

        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        profile.setSomeInfo(request.getSomeInfo());
        myPageProfileRepository.save(profile);

        return ResponseEntity.ok("수정 완료");
    }

    //마이페이지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyPage(@RequestHeader("Authorization") String token){
        String email = extractEmailFromToken(token);

        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        return ResponseEntity.ok("삭제 완료");
    }

    //token에서 email 추출 유틸
    private String extractEmailFromToken(String token){
        return userInfoRequestService.getEmailFromUserToken(token.replace("Bearer ", ""));
    }
}