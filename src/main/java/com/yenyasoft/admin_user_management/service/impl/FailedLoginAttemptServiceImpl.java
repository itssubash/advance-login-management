package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import com.yenyasoft.admin_user_management.service.FailedLoginAttemptService;
import com.yenyasoft.admin_user_management.service.UserApprovalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: FailedLoginAttemptServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class FailedLoginAttemptServiceImpl implements FailedLoginAttemptService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserApprovalHistoryService userApprovalHistoryService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedAttempt(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            user = userMapper.findByUsername(username);
        }
        if (user == null) {
            return;
        }

        if (user.getStatus() == AccountStatus.DISABLED) {
            return;
        }
        if (user.getStatus() != AccountStatus.APPROVED) {
            return;
        }

        Integer currentAttempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        int attempts = currentAttempts + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= 5) {
            user.setStatus(AccountStatus.DISABLED);
            user.setFailedLoginAttempts(attempts);
            userRepository.save(user);
            userApprovalHistoryService.record(
                    user.getUserId(),
                    UserAction.DISABLED,
                    "Exceeded maximum failed login attempts (5)",
                    "SYSTEM"
            );
            return;
        }

        userRepository.save(user);
    }
}
