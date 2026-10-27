package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.enums.LoginStatus;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Author: Subash
 * Created: 10/26/2026
 * File: LoginHistorySearchRequest.java
 *
 */
@Data
public class LoginHistorySearchRequest {
    private Integer userId;
    private int page = 0;
    private int size = 20;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String browser;
    private String operatingSystem;
    private String ipAddress;
    private LoginStatus loginStatus;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private long limit;
    private int offset;


    public void prepare(Pageable pageable) {
        this.fromDateTime = fromDate == null ? null : fromDate.atStartOfDay();
        this.toDateTime = toDate == null ? null : toDate.atTime(LocalTime.MAX);
        this.browser = normalize(browser);
        this.operatingSystem = normalize(operatingSystem);
        this.ipAddress = normalize(ipAddress);
        this.limit = pageable.getPageSize();
        this.offset = (int) pageable.getOffset();
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
