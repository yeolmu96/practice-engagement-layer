package com.backend.account.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_type_id", nullable = false)
    private AccountRoleType roleType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_type_id", nullable = false)
    private AccountLoginType loginType;

    // 기본 생성자
    protected Account() {}

    // 생성자
    public Account(String email, AccountRoleType roleType, AccountLoginType loginType) {
        this.email = email;
        this.roleType = roleType;
        this.loginType = loginType;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public AccountRoleType getRoleType() {
        return roleType;
    }

    public AccountLoginType getLoginType() {
        return loginType;
    }

    // toString
    @Override
    public String toString() {
        return "Account(id=" + id + ", email=" + email + ", roleType=" + roleType + ", loginType=" + loginType + ")";
    }
}
