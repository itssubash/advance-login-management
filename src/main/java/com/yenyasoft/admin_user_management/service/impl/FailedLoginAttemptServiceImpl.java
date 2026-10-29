package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;

import com.yenyasoft.admin_user_management.service.FailedLoginAttemptService;

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

    private static final int MAX_FAILED_ATTEMPTS = 5;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedAttempt(String username) {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userMapper.findByUsername(username));

        if (user == null) {
            return;
        }
        if (user.getStatus() != AccountStatus.APPROVED) {
            return;
        }

        int currentAttempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();

        int attempts = currentAttempts + 1;

        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {user.setStatus(AccountStatus.DISABLED);
        }
        userRepository.save(user);
    }


}
