package com.backend.mypage.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
유저 정보
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MyPageUser {

    //Spring에서 관리할 accountId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // → 생성자에서 createdAt을 따로 설정하지 않아도,
    //   save() 하기 직전에 @PrePersist가 자동 실행되어 createdAt 설정됨
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //생성자
    public MyPageUser(String email) {
        this.email = email;
    }

    //Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
