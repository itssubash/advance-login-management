package com.yenyasoft.admin_user_management.models;

import com.yenyasoft.admin_user_management.enums.LoginStatus;
import com.yenyasoft.admin_user_management.enums.converter.LoginStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: LoginHistory.java
 */
@Entity
@Table(name = "history_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_seq")
    @SequenceGenerator(name = "history_seq", sequenceName = "history_sequence")
    private Integer hId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "User_id",
            foreignKey = @ForeignKey(name = "fk_history_table_user")

    )
    private User user;
    @Column(name = "username", length = 100)
    private String username;
    private boolean successful;
    @Convert(converter = LoginStatusConverter.class)
    @Column(name = "login_status", length = 20)
    private LoginStatus loginStatus;
    private String ipAddress;
    @Column(name = "browser", length = 100)
    private String browser;
    @Column(name = "operating_system", length = 100)
    private String operatingSystem;
    @Column(name = "device", length = 100)
    private String device;
    private String userAgent;
    private String failureReason;
    private LocalDateTime loggedInAt;

    @PrePersist
    public void onCreate() {
        if (loggedInAt == null) {
            loggedInAt = LocalDateTime.now();
        }
        if (loginStatus == null) {
            loginStatus = successful ? LoginStatus.SUCCESS : LoginStatus.FAILED;
        }
    }

}
