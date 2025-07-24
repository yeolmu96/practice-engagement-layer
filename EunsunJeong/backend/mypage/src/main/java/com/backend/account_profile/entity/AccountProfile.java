package com.backend.account_profile.entity;

import com.backend.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, unique = true, nullable = false)
    private String nickname;

    @Column(length = 10)
    private String gender;

    @Column(length = 4)
    private String birthyear;

    @Column(name = "age_range", length = 10)
    private String ageRange;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;
}
