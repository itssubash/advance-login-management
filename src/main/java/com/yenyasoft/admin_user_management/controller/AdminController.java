package com.yenyasoft.admin_user_management.controller;

import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.service.AdminService;
import com.yenyasoft.admin_user_management.service.LoginHistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AdminController.java
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")

public class AdminController extends BaseController {
    private final AdminService adminService;
    private final LoginHistoryService loginHistoryService;

    @GetMapping("/dashboard")
    public ResponseEntity<GlobalResponse<DashboardResponse>> getDashboard() {
        return success(
                adminService.getDashboard(),
                "Dashboard metrics fetched successfully"
        );
    }

    @GetMapping("/users")
    public ResponseEntity<GlobalResponse<Page<UserDto>>> getAllUsers(
            @ModelAttribute UserSearchRequest request) {
        return success(
                adminService.getAllUsers(request),
                "Users fetched successfully"
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<GlobalResponse<UserDetailsResponse>> getUserDetails(@PathVariable Integer id) {
        return success(
                adminService.getUserDetails(id),
                "User details fetched successfully"
        );
    }

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<GlobalResponse<UserDto>> approveUser(
            @PathVariable Integer id,
            Authentication authentication) {
        return success(
                adminService.approveAccount(id, authentication.getName()),
                "User successfully approved, now they can login"
        );
    }

    @PutMapping("/users/{id}/reject")
    public ResponseEntity<GlobalResponse<UserDto>> rejectUser(
            @PathVariable Integer id,
            @Valid @RequestBody RejectRequest request,
            Authentication authentication) {
        return success(
                adminService.rejectAccount(id, request.getReason(), authentication.getName()),
                "User has been rejected"
        );
    }

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<GlobalResponse<UserDto>> disableUser(
            @PathVariable Integer id,
            Authentication authentication) {
        return success(
                adminService.disableAccount(id, authentication.getName()),
                "User account has been disabled"
        );
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<GlobalResponse<UserDto>> enableUser(
            @PathVariable Integer id,
            Authentication authentication) {
        return success(
                adminService.enableAccount(id, authentication.getName()),
                "User account has been enabled"
        );
    }

    @GetMapping("/login-history")
    public ResponseEntity<GlobalResponse<Page<LoginHistoryResponse>>> getAllLoginHistory(
            @ModelAttribute LoginHistorySearchRequest request) {
        return ResponseEntity.ok(
                loginHistoryService.getAllLoginHistory(request)
        );
    }

    @GetMapping("/approved-users-list")
    public ResponseEntity<GlobalResponse<List<UserDto>>> getAllApprovedUsers(){
        return ResponseEntity.ok(
                success(
                        adminService.getAllApprovedUsers(AccountStatus.APPROVED),
                        "Approved users fetched successfully"
                ).getBody()
        );
    }
    @GetMapping("/pending-users-list")
    public ResponseEntity<GlobalResponse<List<UserDto>>> getAllPendingUsers(){
        return ResponseEntity.ok(
                success(
                        adminService.getAllPendingUsers(AccountStatus.PENDING),
                        "Pending users fetched successfully"
                ).getBody()
        );
    }
    @GetMapping("/rejected-users-list")
    public ResponseEntity<GlobalResponse<List<UserDto>>> getAllRejectedUsers(){
        return ResponseEntity.ok(
                success(
                        adminService.getAllRejectedUsers(AccountStatus.REJECTED),
                        "Rejected users fetched successfully"
                ).getBody()
        );
    }
    @GetMapping("/disabled-users-list")
    public ResponseEntity<GlobalResponse<List<UserDto>>> getAllDisabledUsers(){
        return ResponseEntity.ok(
                success(
                        adminService.getAllDisabledUsers(AccountStatus.DISABLED),
                        "Disabled users fetched successfully"
                ).getBody()
        );
    }
    @PutMapping("/reactivate-account")
    public ResponseEntity<GlobalResponse<Void>> reactivateAccount(@Valid @RequestBody ReApproveRequest request){
        adminService.reactivateAccount(request);
        return success(
                null,
                "Your account has been successfully activated. You can now login"
        );
    }

}
