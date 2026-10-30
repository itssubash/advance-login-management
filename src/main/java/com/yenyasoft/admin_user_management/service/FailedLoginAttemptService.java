package com.yenyasoft.admin_user_management.service;

public interface FailedLoginAttemptService {
    boolean recordFailedAttempt(String username);
}
