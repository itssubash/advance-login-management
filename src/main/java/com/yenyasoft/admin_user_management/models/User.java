package com.yenyasoft.admin_user_management.models;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import com.yenyasoft.admin_user_management.enums.converter.AccountStatusConverter;
import com.yenyasoft.admin_user_management.enums.converter.RoleTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: User.java
 */
@Entity
@Table(
        name = "users_table",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = {"email"}
                ),
                @UniqueConstraint(
                        name = "uk_users_username",
                        columnNames = {"username"}
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_sequence", allocationSize = 1)
    private int userId;
    @Column(name = "first_name", nullable = false,  length = 100)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "email",  nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;
    @Column(name = "password", nullable = false,  length = 100)
    private String password;
    @Column(name = "address",length = 100)
    private String address;
    @Convert(converter = RoleTypeConverter.class)
    @Builder.Default
    private RoleType role = RoleType.USER;
    @Convert(converter = AccountStatusConverter.class)
    @Builder.Default
    private AccountStatus status = AccountStatus.PENDING;
    @Column(name = "phone_number", length = 30)
    private String phoneNumber;
    @Column(name = "dob", length = 10)
    private LocalDate dob;

    @Column(name = "rejection_reason", length = 200)
    private String rejectionReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "tokens")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Token> tokens = new ArrayList<>();

    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;
    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
}
