package com.yenyasoft.admin_user_management.config;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AdminInitializer.java
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminEmail = "admin@yenyasoft.com";

        if (userMapper.existsByUsername(adminUsername) || userMapper.existsByEmail(adminEmail)) {
            return;
        }
        User adminUser = User.builder()
                .firstName("System")
                .lastName("Administrator")
                .email(adminEmail)
                .username(adminUsername)
                .password(passwordEncoder.encode("Admin@123"))
                .role(RoleType.ADMIN)
                .status(AccountStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
        userRepository.save(adminUser);

    }
}
