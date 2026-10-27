package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: ResetPasswordRequest.java
 */
@Data
@PasswordMatches
public class ResetPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 72,
            message = "Password must be between 8 and 72 characters")
    private String newPassword;

    @NotNull(message = "Confirm password is required")
    private String confirmNewPassword;


}
