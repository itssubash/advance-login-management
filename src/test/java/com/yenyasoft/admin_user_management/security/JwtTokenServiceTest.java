package com.yenyasoft.admin_user_management.security;

import com.yenyasoft.admin_user_management.models.Token;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService(tokenRepository);
        ReflectionTestUtils.setField(jwtTokenService, "secret", "a-secret-that-is-at-least-32-bytes-long");
        ReflectionTestUtils.setField(jwtTokenService, "issuer", "test-issuer");
        ReflectionTestUtils.setField(jwtTokenService, "accessTtlMs", 600_000L);
        ReflectionTestUtils.setField(jwtTokenService, "refreshTtlMs", 86_400_000L);
    }

    @Test
    void validatesRefreshTokensAgainstRefreshTokenRecords() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("alice")
                .password("unused")
                .authorities("ROLE_USER")
                .build();
        User user = new User();
        user.setUsername("alice");
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        Token storedToken = new Token();
        storedToken.setRefreshToken(refreshToken);
        storedToken.setUser(user);
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(storedToken));

        assertTrue(jwtTokenService.isValidRefreshToken(refreshToken, user));
        verify(tokenRepository).findByRefreshToken(refreshToken);
        verify(tokenRepository, never()).findByAccessToken(anyString());
    }

    @Test
    void rejectsAccessTokensAsRefreshTokens() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("alice")
                .password("unused")
                .authorities("ROLE_USER")
                .build();
        User user = new User();
        user.setUsername("alice");
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        Token storedToken = new Token();
        storedToken.setRefreshToken(accessToken);
        storedToken.setUser(user);
        when(tokenRepository.findByRefreshToken(accessToken)).thenReturn(Optional.of(storedToken));

        assertFalse(jwtTokenService.isValidRefreshToken(accessToken, user));
    }

    @Test
    void createsDistinctRefreshTokensForRapidRotation() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("alice")
                .password("unused")
                .authorities("ROLE_USER")
                .build();

        String firstRefreshToken = jwtTokenService.generateRefreshToken(userDetails);
        String secondRefreshToken = jwtTokenService.generateRefreshToken(userDetails);

        assertNotEquals(firstRefreshToken, secondRefreshToken);
    }

    @Test
    void accessTokenRevocationDoesNotRevokeItsRefreshToken() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("alice")
                .password("unused")
                .authorities("ROLE_USER")
                .build();
        User user = new User();
        user.setUsername("alice");
        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        Token storedToken = new Token();
        storedToken.setAccessToken(accessToken);
        storedToken.setRefreshToken(refreshToken);
        storedToken.setAccessTokenRevoked(true);
        storedToken.setUser(user);
        when(tokenRepository.findByAccessToken(accessToken)).thenReturn(Optional.of(storedToken));
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(storedToken));

        assertFalse(jwtTokenService.isValid(accessToken, userDetails));
        assertTrue(jwtTokenService.isValidRefreshToken(refreshToken, user));
    }
}
