package com.yenyasoft.admin_user_management.controller;

import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AuthController.java
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<UserDto>> registerUser(@Valid @RequestBody RegisterRequest request) {
        return created(
                authService.register(request),
                "User registered successfully. Waiting for the admin approval"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        LoginResponse loginResponse = authService.login(request, httpRequest);
        return success(
                loginResponse,
                "Login successful"
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<GlobalResponse<String>> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {

        if (isInvalidAuthorization(authorization)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "A valid Bearer access token is required"
            );
        }

        String accessToken = authorization.substring("Bearer ".length()).trim();
        authService.logout(accessToken);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(
                GlobalResponse.success(
                        "Logged out successfully",
                        null
                )
        );
    }
    private boolean isInvalidAuthorization(String authorization) {
        return !StringUtils.hasText(authorization)
                || !authorization.startsWith("Bearer ")
                || !StringUtils.hasText(authorization.substring("Bearer ".length()).trim());
    }

    @PostMapping("/regenerate-tokens")
    public ResponseEntity<GlobalResponse<LoginResponse>> regenerateToken(@Valid @RequestBody NewTokenRequest request) {
        return success(
                authService.regenerateTokens(request),
                "New token pairs regenerated successfully"
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<GlobalResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(authentication.getName(), request);
        return success(
                null,
                "Password changed successfully."
        );
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<GlobalResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ){
        GlobalResponse<String> response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<GlobalResponse<Void>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ){
        authService.verifyOtp(request);
        return ResponseEntity.ok(
                GlobalResponse.success(
                        "Your otp has been verified successfully",
                         null
                )
        );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<GlobalResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ){
        authService.resetPassword(request);
        return ResponseEntity.ok(
                GlobalResponse.success(
                        "Your password has been reset successfully",
                        null
                )
        );
    }


}
