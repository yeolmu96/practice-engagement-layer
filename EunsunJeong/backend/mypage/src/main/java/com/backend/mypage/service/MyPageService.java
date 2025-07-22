package com.backend.mypage.service;

import com.backend.mypage.controller.request_form.MyPageProfileRequest;
import com.backend.mypage.controller.response_form.MyPageProfileResponse;
import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.entitiy.MyPageUser;
import com.backend.mypage.exception.UnauthorizedException;
import com.backend.mypage.repository.MyPageProfileRepository;
import com.backend.mypage.repository.MyPageUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserInfoRequestService userInfoRequestService;
    private final MyPageUserRepository myPageUserRepository;
    private final MyPageProfileRepository myPageProfileRepository;

    public MyPageProfile initializeMypage(String userToken){
        String email = userInfoRequestService.getEmailFromUserToken(userToken);
        if(email == null){
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        //Django에서 가입한 유저 정보를 바탕으로, Spring 시스템에 필요한 유저 레코드를 생성
        MyPageUser user = myPageUserRepository.findByEmail(email)
                //회원가입 아니고 마이페이지 관리를 위한 초기화 단계
                .orElseGet(() -> myPageUserRepository.save(new MyPageUser(email)));

        //마이페이지 조회 또는 생성
        return myPageProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    MyPageProfile newMypage = new MyPageProfile();
                    newMypage.setUser(user);
                    return myPageProfileRepository.save(newMypage);
                });
    }

    public MyPageProfileResponse getMyPageByEmail(String email){
        MyPageProfile profile = findProfileByEmail(email);
        return MyPageProfileResponse.from(profile);
    }

    public void updateMyPage(String email, MyPageProfileRequest request){
        MyPageProfile profile = findProfileByEmail(email);
        profile.setSomeInfo(request.getSomeInfo());
        myPageProfileRepository.save(profile);
    }

    public void deleteMyPage(String email){
        MyPageProfile profile = findProfileByEmail(email);
        myPageProfileRepository.delete(profile);
    }

    //공통 유틸 메소드
    private MyPageUser findUserByEmail(String email){
        return myPageUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    private MyPageProfile findProfileByEmail(String email){
        MyPageUser user = findUserByEmail(email);
        return myPageProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("마이페이지가 존재하지 않습니다."));
    }
}
