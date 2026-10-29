package com.yenyasoft.admin_user_management.service;

import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AuthService.java
 */
public interface AuthService {
    UserDto register(RegisterRequest request);
    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest);
    void changePassword(String username, PasswordChangeRequest request);
    GlobalResponse<UserProfileResponse> getUserProfile(String username);
    GlobalResponse<UserProfileResponse> updateProfile(String username, UpdateProfileRequest request);
    GlobalResponse<String> forgotPassword(ForgotPasswordRequest request);
    void verifyOtp(VerifyOtpRequest request);
    void resetPassword(ResetPasswordRequest request);
    void logout(String accessToken);
    LoginResponse regenerateTokens(NewTokenRequest request);

}
