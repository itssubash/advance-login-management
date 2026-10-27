package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.converter.UserConverter;
import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.LoginStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.models.Otp;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.OtpRepository;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import com.yenyasoft.admin_user_management.security.JwtTokenService;
import com.yenyasoft.admin_user_management.service.*;
import com.yenyasoft.admin_user_management.utils.PasswordChangeValidator;
import com.yenyasoft.admin_user_management.utils.UserAgentUtils;
import com.yenyasoft.admin_user_management.utils.UserRegistrationValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;


/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AuthServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginHistoryMapper loginHistoryMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final LoginHistoryService loginHistoryService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final OtpRepository otpRepository;
    private final FailedLoginAttemptService failedLoginAttemptService;

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        validator.validate(request, userMapper);

        User user = new User();
        user.setFirstName(trim(request.getFirstName()));
        user.setLastName(trim(request.getLastName()));
        user.setEmail(trim(request.getEmail()));
        user.setUsername(trim(request.getUsername()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(trim(request.getAddress()));
        user.setPhoneNumber(trim(request.getPhoneNumber()));
        user.setDob(request.getDob());
        user.setRole(RoleType.USER);
        user.setStatus(AccountStatus.PENDING);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return UserConverter.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        String username = trim(request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseGet(() -> userMapper.findByUsername(userDetails.getUsername()));
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found");
            }

            if (user.getFailedLoginAttempts() != null && user.getFailedLoginAttempts() != 0) {
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(null);
                userRepository.save(user);
            }

            saveLoginHistory(user, username, true, null, httpRequest);

            String accessToken = jwtTokenService.generateAccessToken(userDetails);
            String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

            return LoginResponse.builder()
                    .user(UserConverter.toUserDto(user))
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        catch (InternalAuthenticationServiceException e) {
            if (isDisabledAccount(e)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Your account has been disabled due to 5 consecutive unsuccessful login attempts."
                );
            }
            throw e;

        } catch (DisabledException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account has been disabled. Please contact the administrator."
            );

        } catch (BadCredentialsException e) {
            failedLoginAttemptService.recordFailedAttempt(username);

            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userMapper.findByUsername(username));

            if (user != null && user.getStatus() == AccountStatus.DISABLED) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Your account has been disabled due to 5 consecutive unsuccessful login attempts."
                );
            }

            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private boolean isDisabledAccount(Throwable exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof DisabledException) {
                return true;
            }
            cause = cause.getCause();
        }

        return "User account is disabled".equals(exception.getMessage());
    }


    @Override
    @Transactional
    public void changePassword(String username, PasswordChangeRequest request) {
        PasswordChangeValidator.validatePassword(request);
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from the current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        GlobalResponse.success(
                "Password changed successfully",
                null
        );
    }

    @Override
    public GlobalResponse<UserProfileResponse> getUserProfile(String username) {
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        UserProfileResponse profile = buildProfile(currentUser);
        return GlobalResponse.success(
                "User profile retrieved successfully",
                profile
        );
    }

    @Override
    @Transactional
    public GlobalResponse<UserProfileResponse> updateProfile(String username, UpdateProfileRequest request) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName().trim());
        }
        if (request.getAddress() != null) {
            user.setAddress(trim(request.getAddress()));
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(trim(request.getPhoneNumber()));
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return GlobalResponse.success(
                "Profile updated successfully",
                buildProfile(updatedUser)
        );
    }

    @Override
    public GlobalResponse<String> forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();

        if (userMapper.findByEmail(email) == null) {
            return GlobalResponse.success(
                    "If the email is registered, password reset instructions will be sent.",
                    null
            );
        }
        String otp = otpService.generateOtp();

        Otp resetOtp = otpRepository.findByEmail(email).orElseGet(Otp::new);
        resetOtp.setEmail(email);
        resetOtp.setOtpHash(passwordEncoder.encode(otp));
        resetOtp.setExpiresAt(Instant.now().plusSeconds(300));
        resetOtp.setVerified(false);
        otpRepository.save(resetOtp);
        emailService.sendOtp(email, otp);

        return GlobalResponse.success(
                "The password reset instructions has been sent ",
                null
        );
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        String email = request.getEmail();
        Otp resetOtp = otpRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));

        if (resetOtp.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }
        resetOtp.setVerified(true);
        otpRepository.save(resetOtp);

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();

        Otp resetOtp = otpRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Please verify your otp first "));

        if (!resetOtp.isVerified() || resetOtp.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("OTP verification is required or has expired");
        }
        User user = userMapper.findByEmail(email);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        otpRepository.delete(resetOtp);

    }

    private UserProfileResponse buildProfile(User user) {
        LoginHistory lastLogin = loginHistoryMapper.findLatestByUserId(user.getUserId());
        long totalLogins = loginHistoryMapper.countByUserId(user.getUserId());
        return UserConverter.toUserProfile(
                user,
                lastLogin != null ? lastLogin.getLoggedInAt() : null,
                totalLogins
        );
    }

    private void saveLoginHistory(User user, String username, boolean successful,
                                  String failureReason, HttpServletRequest httpRequest) {
        String userAgent = httpRequest.getHeader("User-Agent");
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setUsername(username);
        history.setSuccessful(successful);
        history.setLoginStatus(successful ? LoginStatus.SUCCESS : LoginStatus.FAILED);
        history.setIpAddress(httpRequest.getRemoteAddr());
        history.setUserAgent(userAgent);
        history.setBrowser(UserAgentUtils.resolveBrowser(userAgent));
        history.setOperatingSystem(UserAgentUtils.resolveOperatingSystem(userAgent));
        history.setDevice(UserAgentUtils.resolveDevice(userAgent));
        history.setFailureReason(failureReason);
        loginHistoryService.saveLoginHistory(history);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
