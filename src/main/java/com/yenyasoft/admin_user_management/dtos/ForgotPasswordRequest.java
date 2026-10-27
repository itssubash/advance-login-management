package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: ForgotPasswordRequest.java
 */
@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provider a valid email address")
    private String email;
}
