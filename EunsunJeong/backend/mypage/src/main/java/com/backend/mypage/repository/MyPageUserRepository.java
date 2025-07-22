package com.backend.mypage.repository;

import com.backend.mypage.entitiy.MyPageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyPageUserRepository extends JpaRepository<MyPageUser, Long> {

    //email로 사용자 조회(중복 가입 방지)
    Optional<MyPageUser> findByEmail(String email);

    //email 존재 여부 확인
    boolean existsByEmail(String email);
}
