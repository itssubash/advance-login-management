package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: LoginResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private UserDto user;
    private String accessToken;
    private String refreshToken;
}
