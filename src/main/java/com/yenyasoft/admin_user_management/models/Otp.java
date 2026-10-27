package com.yenyasoft.admin_user_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: Otp.java
 */
@Entity
@Table(name = "password_reset_otp_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_seq")
    @SequenceGenerator(name = "otp_sequ", sequenceName = "otp_sequence")
    private int id;
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    @Column(name = "otp_hash", nullable = false, length = 100)
    private String otpHash;
    private Instant expiresAt;
    private int attempts;
    private boolean verified;
    private String resetTokenHash;
    private Instant resetExpiresAt;
}
