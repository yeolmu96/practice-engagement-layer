package com.backend.mypage.repository;

import com.backend.mypage.entitiy.MyPageProfile;
import com.backend.mypage.entitiy.MyPageUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageProfileRepository extends JpaRepository<MyPageProfile, Long> {
    Optional<MyPageProfile> findByUser(MyPageUser user);
}
