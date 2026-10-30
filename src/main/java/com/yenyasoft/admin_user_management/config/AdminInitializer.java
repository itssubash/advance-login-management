package com.yenyasoft.admin_user_management.config;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/30/2026
 * File: AdminInitializer.java
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@yenyasoft.com";
        String adminUsername = "admin";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }
        User adminUser = User.builder()
                .firstName("System")
                .lastName("Administrator")
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode("Admin@123"))
                .role(RoleType.ADMIN)
                .status(AccountStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
        userRepository.save(adminUser);

        System.out.println("Default admin user created successfully");

    }
}
