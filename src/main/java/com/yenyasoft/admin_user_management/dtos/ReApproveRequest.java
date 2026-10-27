package com.yenyasoft.admin_user_management.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: ReApproveRequest.java
 */
@Data
public class ReApproveRequest {
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
}
