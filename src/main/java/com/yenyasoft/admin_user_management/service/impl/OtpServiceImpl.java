package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.service.OtpService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: OtpServiceImpl.java
 */
@Service
public class OtpServiceImpl implements OtpService {
    private final SecureRandom random = new SecureRandom();
    @Override
    public String generateOtp() {
        int otp = random.nextInt(1_000_000);
        return String.format("%06d", otp);
    }
}
