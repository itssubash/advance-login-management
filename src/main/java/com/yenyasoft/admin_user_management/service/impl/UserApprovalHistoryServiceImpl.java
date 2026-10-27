package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.models.UserApprovalHistory;
import com.yenyasoft.admin_user_management.repository.UserApprovalHistoryRepository;
import com.yenyasoft.admin_user_management.service.UserApprovalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistoryServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class UserApprovalHistoryServiceImpl implements UserApprovalHistoryService {
    private final UserApprovalHistoryRepository userApprovalHistoryRepository;
    private final UserMapper userMapper;

    @Override
    public void record(Integer userId, UserAction action, String reason, String performedBy) {
        User user = userMapper.findById(userId);
        UserApprovalHistory history = UserApprovalHistory.builder()
                .user(user)
                .action(action)
                .reason(reason)
                .performedBy(resolvePerformedBy(performedBy))
                .build();
        userApprovalHistoryRepository.save(history);
    }

    private String resolvePerformedBy(String performedBy) {
        if (performedBy != null && !performedBy.isBlank()) {
            return performedBy;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "SYSTEM";
        }
        return authentication.getName();
    }
}
