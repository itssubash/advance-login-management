package com.yenyasoft.admin_user_management.validation;

import com.yenyasoft.admin_user_management.dtos.RegisterRequest;
import com.yenyasoft.admin_user_management.dtos.ResetPasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: PasswordMatchesValidator.java
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof RegisterRequest request) {
            return matches(
                    request.getPassword(),
                    request.getConfirmPassword(),
                    "confirmPassword",
                    context
            );
        }

        if (value instanceof ResetPasswordRequest request) {
            return matches(
                    request.getNewPassword(),
                    request.getConfirmNewPassword(),
                    "confirmNewPassword",
                    context
            );
        }

        return true;
    }

    private boolean matches(
            String password,
            String confirmation,
            String field,
            ConstraintValidatorContext context) {

        if (password == null || confirmation == null) {return true;}
        if (password.equals(confirmation)) {return true;}

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        "Passwords do not match")
                .addPropertyNode(field)
                .addConstraintViolation();

        return false;
    }
}
