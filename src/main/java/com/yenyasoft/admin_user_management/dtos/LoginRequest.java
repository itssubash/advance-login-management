package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: LoginRequest.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username can not exceed 100 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    private String password;
}
