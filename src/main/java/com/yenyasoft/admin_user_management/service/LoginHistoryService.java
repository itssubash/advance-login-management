package com.yenyasoft.admin_user_management.service;

import com.yenyasoft.admin_user_management.dtos.GlobalResponse;
import com.yenyasoft.admin_user_management.dtos.LoginHistoryResponse;
import com.yenyasoft.admin_user_management.dtos.LoginHistorySearchRequest;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import org.springframework.data.domain.Page;

/**
 * Author: Subash
 * Created: 10/13/2026
 * File: LoginHistoryService.java
 */
public interface LoginHistoryService {
    void saveLoginHistory(LoginHistory loginHistory);

    GlobalResponse<Page<LoginHistoryResponse>> getMyLoginHistory(LoginHistorySearchRequest request);

    GlobalResponse<Page<LoginHistoryResponse>> getAllLoginHistory(LoginHistorySearchRequest request);
}
