package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: OtpRepository.java
 */
public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
 }
