package com.yenyasoft.admin_user_management.utils;

import com.yenyasoft.admin_user_management.dtos.RegisterRequest;
import com.yenyasoft.admin_user_management.exceptions.AlreadyExistsException;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import org.springframework.stereotype.Component;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: UserRegistrationValidator.java
 */
@Component
public class UserRegistrationValidator {
    public void validate(RegisterRequest request, UserMapper userMapper) {
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException(
                    "Email is already registered"
            );
        }

        if (userMapper.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(
                    "Username is already taken"
            );
        }
    }}
