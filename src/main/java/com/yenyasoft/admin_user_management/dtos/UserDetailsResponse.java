package com.yenyasoft.admin_user_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserDetailsResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private UserDto user;
    private LocalDateTime lastLogin;
    private long totalLogins;
}
