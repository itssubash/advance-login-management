package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: RejectRequest.java
 */
@Data
public class RejectRequest {
    @NotBlank(message = "Rejection reason is required")
    @Size(max = 200, message = "Rejection reason cannot exceed 200 characters")
    private String reason;
}
