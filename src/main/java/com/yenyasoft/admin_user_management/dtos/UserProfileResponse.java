package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserProfileResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String address;
    private String phoneNumber;
    private LocalDate dob;
    private RoleType role;
    private AccountStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime lastLogin;
    private long totalLogins;
}
