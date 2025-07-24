package com.backend.userdashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 유저 대시보드 엔티티
 * - Django의 Account와 직접 연동하지 않고, accountId (Long)만 참조
 * - 레벨, 경험치, 신뢰지수 등을 저장하여 대시보드 핵심 정보 제공
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 장고 Account 테이블의 PK를 참조하는 값
     * - 실제 Account 객체를 연동하지 않고 ID만 저장
     * - Redis 또는 연동 API에서 이메일 기반으로 accountId를 얻어와 사용
     */
    @JoinColumn(nullable = false)
    private Long accountId;

    /** 유저의 레벨 (활동 기반) */
    private int level;

    /** 유저의 경험치 (XP) */
    private int xp;

    /** 유저의 신뢰 점수 (스터디 기여도, 출석률 등 기반 점수화 가능) */
    private int trustScore;

    /** 생성 시각 */
    private LocalDateTime createdAt;

    /**
     * 최초 저장 시점에 자동으로 생성시간을 기록
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
