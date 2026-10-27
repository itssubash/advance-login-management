package com.yenyasoft.admin_user_management.service;

import com.yenyasoft.admin_user_management.enums.UserAction;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistoryService.java
 */
public interface UserApprovalHistoryService {
    void record(Integer userId, UserAction action, String reason, String performedBy);
}
