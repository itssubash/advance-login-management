package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: TokenPair.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    private String accessToken;
    private String refreshToken;
}
