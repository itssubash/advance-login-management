package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.models.UserApprovalHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistoryResponse.java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApprovalHistoryResponse {
    private int id;
    private Integer userId;
    private UserAction action;
    private String reason;
    private String performedBy;
    private LocalDateTime performedAt;

    public static UserApprovalHistoryResponse toResponse(UserApprovalHistory history) {
        UserApprovalHistoryResponse response = new UserApprovalHistoryResponse();
        response.setId(history.getApprovalId());
        response.setUserId(history.getUser() != null
                ? history.getUser().getUserId()
                : null);
        response.setAction(history.getAction());
        response.setReason(history.getReason());
        response.setPerformedBy(history.getPerformedBy());
        response.setPerformedAt(history.getPerformedAt());
        return response;
    }
}
