package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: EmailServiceImpl.java
 */
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserMapper userMapper;
    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${spring.application.name}")
    private String appName;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, UserMapper userMapper) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userMapper = userMapper;
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

    @Override
    public void sendActivationMail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }
        String recipientName = user.getFirstName() + " " + user.getLastName();
        Context context = new Context();
        context.setVariable("recipientName", recipientName);
        context.setVariable("appName", appName);
        String htmlContent = templateEngine.process("account-activation", context);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject("Account Activation");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException exception) {
            throw new IllegalArgumentException("Failed to send the activation message", exception);
        }
    }

    @Override
    public void sendDisabledMail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }
        String recipientName = user.getFirstName() + " " + user.getLastName();

        Context context = new Context();
        context.setVariable("recipientName", recipientName);
        context.setVariable("appName", appName);

        String htmlContent = templateEngine.process("account-disable", context);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject("Account Disabled");
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException exception) {
            throw new IllegalArgumentException("Failed to send the account disabled message", exception);
        }
    }

    @Override
    public void sendRejectedMail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }
        String recipientName = user.getFirstName() + " " + user.getLastName();

        Context context = new Context();
        context.setVariable("recipientName", recipientName);
        context.setVariable("appName", appName);

        String htmlContent = templateEngine.process("account-reject", context);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject("Account Rejection");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Failed to send account rejection mail", e);
        }
    }
}
