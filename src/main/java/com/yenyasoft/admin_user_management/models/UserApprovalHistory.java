package com.yenyasoft.admin_user_management.models;

import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.enums.converter.UserActionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistory.java
 */
@Entity
@Table(name = "user_approval_history_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApprovalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approval_seq")
    @SequenceGenerator(name = "approval_seq", sequenceName = "approval_sequence", allocationSize = 1)
    private Integer approvalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_approval_history_user")
    )
    private User user;

    @Convert(converter = UserActionConverter.class)
    @Column(name = "action", length = 20, nullable = false)
    private UserAction action;

    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @PrePersist
    public void onCreate() {
        if (performedAt == null) {
            performedAt = LocalDateTime.now();
        }
    }
}
