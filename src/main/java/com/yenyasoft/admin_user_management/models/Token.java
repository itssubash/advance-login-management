package com.yenyasoft.admin_user_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Subash
 * Created: 10/29/2026
 * File: Token.java
 */
@Entity
@Table(name = "token_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_sequence")
    private int tokenId;
    @Column(name = "access_token", length = 2000)
    private String accessToken;
    @Column(name = "refresh_token", length = 2000)
    private String refreshToken;

    @Column(name = "is_logged_out")
    private boolean isLoggedOut = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
