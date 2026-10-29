package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author: Subash
 * Created: 10/29/2026
 * File: TokenRepository.java
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
           SELECT t
           FROM Token t
           WHERE t.user.userId = :userId
           AND t.isLoggedOut = false
           """)
    List<Token> findAllActiveTokensByUserId(
            @Param("userId") Integer userId
    );
    Optional<Token> findByAccessToken(String token);
    Optional<Token> findByRefreshToken(String refreshToken);
}
