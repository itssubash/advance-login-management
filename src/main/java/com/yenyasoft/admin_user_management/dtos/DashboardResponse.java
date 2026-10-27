package com.yenyasoft.admin_user_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: DashboardResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private long totalUsers;
    private long pendingUsers;
    private long approvedUsers;
    private long rejectedUsers;
    private long disabledUsers;
    private long todayLogins;
    private long thisMonthLogins;
}
