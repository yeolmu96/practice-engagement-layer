package com.backend.account_profile.repository;

import com.backend.account_profile.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProfileJpaRepository extends JpaRepository<AdminProfile, Long> {
}