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
 * Created: 10/12/2026
 * File: UserDto.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String address;
    private String phoneNumber;
    private LocalDate dob;
    private RoleType role;
    private AccountStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
}
