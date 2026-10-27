package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: VerifyOtpRequest.java
 */
@Data
public class VerifyOtpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Please enter the 6 digits otp sent to your email")
    private String otp;
}
