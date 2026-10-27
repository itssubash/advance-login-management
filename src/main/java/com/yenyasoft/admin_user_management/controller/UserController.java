package com.yenyasoft.admin_user_management.controller;

import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.service.AuthService;
import com.yenyasoft.admin_user_management.service.LoginHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserController.java
 */
@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final AuthService authService;
    private final LoginHistoryService loginHistoryService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<GlobalResponse<UserProfileResponse>> getProfile(Authentication authentication) {
        return ResponseEntity.ok(
                authService.getUserProfile(authentication.getName())
        );
    }

    @PutMapping
    public ResponseEntity<GlobalResponse<UserProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(
                authService.updateProfile(authentication.getName(), request)
        );
    }

    @GetMapping("/login-history")
    public ResponseEntity<GlobalResponse<Page<LoginHistoryResponse>>> getLoginHistory(
            Authentication authentication,
            @ModelAttribute LoginHistorySearchRequest request) {
        User user = getAuthenticatedUser(authentication);
        request.setUserId(user.getUserId());
        return ResponseEntity.ok(
                loginHistoryService.getMyLoginHistory(request)
        );
    }

    private User getAuthenticatedUser(Authentication authentication) {
        User user = userMapper.findByUsername(authentication.getName());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Authenticated user not found");
        }
        return user;
    }

}
