package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FailedLoginAttemptServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FailedLoginAttemptServiceImpl failedLoginAttemptService;

    @Test
    void locksApprovedUserAfterFifthFailedAttemptForFifteenMinutes() {
        User user = approvedUser();
        user.setFailedLoginAttempts(4);
        when(userRepository.findByUsernameForUpdate("alice")).thenReturn(Optional.of(user));

        assertTrue(failedLoginAttemptService.recordFailedAttempt("alice"));

        assertEquals(5, user.getFailedLoginAttempts());
        assertEquals(AccountStatus.APPROVED, user.getStatus());
        assertNotNull(user.getLockedUntil());
        assertTrue(Duration.between(Instant.now(), user.getLockedUntil()).toSeconds() <= 900);
        assertTrue(Duration.between(Instant.now(), user.getLockedUntil()).toSeconds() > 890);
        verify(userRepository).save(user);
    }

    @Test
    void doesNotLockBeforeFifthFailedAttempt() {
        User user = approvedUser();
        user.setFailedLoginAttempts(3);
        when(userRepository.findByUsernameForUpdate("alice")).thenReturn(Optional.of(user));

        assertFalse(failedLoginAttemptService.recordFailedAttempt("alice"));

        assertEquals(4, user.getFailedLoginAttempts());
        assertEquals(AccountStatus.APPROVED, user.getStatus());
        assertNull(user.getLockedUntil());
    }

    @Test
    void doesNotCountFailedAttemptsForUnapprovedAccounts() {
        User user = approvedUser();
        user.setStatus(AccountStatus.PENDING);
        when(userRepository.findByUsernameForUpdate("alice")).thenReturn(Optional.of(user));

        assertFalse(failedLoginAttemptService.recordFailedAttempt("alice"));

        assertEquals(0, user.getFailedLoginAttempts());
    }

    private User approvedUser() {
        User user = new User();
        user.setUsername("alice");
        user.setStatus(AccountStatus.APPROVED);
        user.setFailedLoginAttempts(0);
        return user;
    }
}
