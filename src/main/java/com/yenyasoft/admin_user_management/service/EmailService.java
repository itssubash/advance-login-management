package com.yenyasoft.admin_user_management.service;


/**
 * Author: Subash
 * Created: 10/27/2026
 * File: EmailService.java
 */
public interface EmailService {
    void sendOtp(String email, String otp);
    void sendActivationMail(String email);
    void sendDisabledMail(String email);
    void sendRejectedMail(String email);

}
