package com.yenyasoft.admin_user_management.service;

public interface FailedLoginAttemptService {
    void recordFailedAttempt(String username);
}
