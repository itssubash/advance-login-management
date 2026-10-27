package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/13/2026
 * File: PasswordChangeRequest.java
 */
@Data
public class PasswordChangeRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 72, message = "Password should be between 8 and 72 characters")
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    private String confirmNewPassword;
}
