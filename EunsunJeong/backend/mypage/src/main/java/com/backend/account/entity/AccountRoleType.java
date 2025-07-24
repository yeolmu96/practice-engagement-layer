package com.backend.account.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account_role_type")
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

    public Long getId() {
        return id;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public String toString() {
        return roleType != null ? roleType.name() : "null";
    }
}
