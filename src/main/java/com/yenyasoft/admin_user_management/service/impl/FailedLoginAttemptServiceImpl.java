package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;

import com.yenyasoft.admin_user_management.service.FailedLoginAttemptService;

import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MINUTES = 15;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean recordFailedAttempt(String username) {
        User user = userRepository.findByUsernameForUpdate(username).orElse(null);
        if (user == null) {
            return false;
        }
        if (user.getStatus() != AccountStatus.APPROVED) {
            return false;
        }

        int currentAttempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        int attempts = currentAttempts + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockedUntil(Instant.now().plus(LOCK_DURATION_MINUTES, ChronoUnit.MINUTES));
        }
        userRepository.save(user);
        return attempts >= MAX_FAILED_ATTEMPTS;
    }


}
