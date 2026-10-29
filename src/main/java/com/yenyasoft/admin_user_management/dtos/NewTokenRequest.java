package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Author: Subash
 * Created: 10/29/2026
 * File: NewTokenRequest.java
 */
@Data
public class NewTokenRequest {
    @NotBlank(message = "Provide a valid refresh token")
    private String refreshToken;
}
