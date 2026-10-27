package com.yenyasoft.admin_user_management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: PasswordMatches.java
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
