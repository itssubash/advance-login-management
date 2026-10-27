package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: EmailServiceImpl.java
 */
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private  String fromAddress;

    @Value("${spring.application.name}")
    private String appName;
    public EmailServiceImpl(JavaMailSender mailSender,  TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    @Override
    public void sendOtp(String email, String otp) {
        Context context = new Context();
        context.setVariable("recipientName", "User");
        context.setVariable("otp", otp);
        context.setVariable("expiryMinutes", 5);
        context.setVariable("appName", appName);

        String htmlContent = templateEngine.process("password-reset-otp", context);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject("Password Reset OTP");
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send OTP email", e);
        }
    }
}
