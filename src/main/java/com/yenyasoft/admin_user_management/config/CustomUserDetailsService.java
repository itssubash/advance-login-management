package com.yenyasoft.admin_user_management.config;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: CustomUserDetailsService.java
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (user.getStatus() == AccountStatus.DISABLED){
            throw new DisabledException("User account is disabled");
        }
        if (user.getStatus() != AccountStatus.APPROVED){
            throw new DisabledException("User account has not been approved yet");
        }
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new LockedException("Account is temporarily locked due to too many failed login attempts");
        }

        return new CustomUserDetails(user);
    }
}
