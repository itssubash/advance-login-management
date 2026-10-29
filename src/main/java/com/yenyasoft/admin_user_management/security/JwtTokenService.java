package com.yenyasoft.admin_user_management.security;

import com.yenyasoft.admin_user_management.dtos.TokenPair;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: JwtTokenService.java
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final TokenRepository tokenRepository;

    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-issuer}")
    private String issuer;

    @Value("${app.jwt-access-expiry}")
    private long accessTtlMs;

    @Value("${app.jwt-refresh-expiry}")
    private long refreshTtlMs;

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    // generate access tokens
    public String generateAccessToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "AccessToken");

        return generateToken(user, accessTtlMs, claims);
    }

    //generate refresh tokens
    public String generateRefreshToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "RefreshToken");
        return generateToken(user, refreshTtlMs, claims);
    }

    // generate token pair
    public TokenPair generateTokenPair(UserDetails user) {
        return new TokenPair(
                generateAccessToken(user),
                generateRefreshToken(user)
        );

    }

    // generic method for token generation
    public String generateToken(UserDetails userDetails, long expiry, Map<String, Object> claims) {
        Date now = new Date();
        Map<String, Object> map = new HashMap<>(claims);
        String role = userDetails.getAuthorities().
                stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse("ROLE_USER");
        map.put("role", role);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiry))
                .claims(map)
                .signWith(getSigningKey())
                .compact();
    }

    // extract claims
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid or expired JWT token", ex);
        }
    }

    //extract subject/username
    public String getSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        String username = getSubject(token);
        boolean validToken = tokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isValidRefreshToken(String token, User user) {
        String username = getSubject(token);
        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validRefreshToken;
    }







}




