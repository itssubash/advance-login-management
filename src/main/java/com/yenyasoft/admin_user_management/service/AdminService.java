package com.yenyasoft.admin_user_management.service;

import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.enums.AccountStatus;
import org.springframework.data.domain.Page;


import java.util.List;

/**
 * Author: Subash
 * Created: 10/13/2026
 * File: AdminService.java
 */
public interface AdminService {
    Page<UserDto> getAllUsers(UserSearchRequest request);

    UserDetailsResponse getUserDetails(Integer userId);

    UserDto approveAccount(Integer userId, String performedBy);

    UserDto rejectAccount(Integer userId, String reason, String performedBy);

    UserDto disableAccount(Integer userId, String performedBy);

    UserDto enableAccount(Integer userId, String performedBy);

    DashboardResponse getDashboard();

    List<UserDto> getAllApprovedUsers(AccountStatus status);

    void reactivateAccount(ReApproveRequest  request);
}
