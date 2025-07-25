package com.backend.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "account_role_type")
@Getter
@ToString
public class AccountRoleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", length = 64, nullable = false)
    private RoleType roleType;

    protected AccountRoleType() {}

    public AccountRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
