package com.yenyasoft.admin_user_management.utils;

import com.yenyasoft.admin_user_management.dtos.PasswordChangeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


/**
 * Author: Subash
 * Created: 10/13/2026
 * File: PasswordChangeValidator.java
 */

public class PasswordChangeValidator {
    private PasswordChangeValidator() {
    }

    public static void validatePassword(PasswordChangeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password Change request is required"
            );
        }
        String currentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();
        String confirmNewPassword = request.getConfirmNewPassword();
        if (isBlank(currentPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
        }
        if (newPassword.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 8 characters");
        }

        if (!newPassword.matches(".*[A-Z].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must contain an uppercase letter");
        }

        if (!newPassword.matches(".*[a-z].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must contain a lowercase letter");
        }

        if (!newPassword.matches(".*\\d.*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must contain a number");
        }

        if (!newPassword.matches(".*[^a-zA-Z0-9].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must contain a special character");
        }

        if (isBlank(confirmNewPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirm password is required");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirmation do not match");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

}

