package com.backend.mypage.controller;

import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.entitiy.MyPageUser;
import com.backend.mypage.repository.MyPageProfileRepository;
import com.backend.mypage.repository.MyPageUserRepository;
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
    private final MyPageProfileRepository myPageProfileRepository;
    private final MyPageUserRepository myPageUserRepository;

    //마이페이지 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrGetMypage(@RequestHeader("Authorization") String token) {
        log.info("받은 토큰: {}", token);
        MyPageProfile profile = myPageService.initializeMypage(token.replace("Bearer ", ""));
        return ResponseEntity.ok(profile);
    }

    //마이페이지 조회
    @GetMapping("/get")
    public ResponseEntity<MyPageProfileResponse> getMyPage(@RequestParam String email){
        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageUserRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        return ResponseEntity.ok(MyPageProfileResponse.from(profile));
    }

    //마이페이지 수정
    @PutMapping("/edit")
    public ResponseEntity<String> updateMyPage(@RequestParam String email,
                                               @RequestBody MyPageProfileReqeust request){
        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        profile.setSomeInfo(request.getSomeInfo());
        myPageProfileRepository.save(profile);

        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMyPage(@RequestParam String email){
        MyPageUser user = myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        MyPageProfile profile = myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));

        return ResponseEntity.ok("삭제 완료");
    }
}