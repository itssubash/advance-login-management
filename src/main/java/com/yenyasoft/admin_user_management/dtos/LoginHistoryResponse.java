package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.enums.LoginStatus;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: LoginHistoryResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistoryResponse {
    private int id;
    private Integer userId;
    private String username;
    private boolean successful;
    private LoginStatus loginStatus;
    private String ipAddress;
    private String browser;
    private String operatingSystem;
    private String device;
    private String userAgent;
    private String failureReason;
    private LocalDateTime loginTime;

    public static LoginHistoryResponse toResponse(LoginHistory history) {
        LoginHistoryResponse response = new LoginHistoryResponse();
        response.setId(history.getHId());
        response.setUserId(history.getUser() != null
                ? history.getUser().getUserId()
                : null);
        response.setUsername(history.getUsername());
        response.setSuccessful(history.isSuccessful());
        response.setLoginStatus(history.getLoginStatus());
        response.setIpAddress(history.getIpAddress());
        response.setBrowser(history.getBrowser());
        response.setOperatingSystem(history.getOperatingSystem());
        response.setDevice(history.getDevice());
        response.setUserAgent(history.getUserAgent());
        response.setFailureReason(history.getFailureReason());
        response.setLoginTime(history.getLoggedInAt());
        return response;
    }
}
