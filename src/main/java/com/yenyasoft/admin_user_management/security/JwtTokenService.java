package com.yenyasoft.admin_user_management.security;

import com.yenyasoft.admin_user_management.dtos.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
public class JwtTokenService {

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


    //validate token expiry
    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration() == null || !claims.getExpiration().after(new Date());
    }

    //validating the subjects of the token
    public boolean isTokenValid(String token, UserDetails user) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date())
                    && "AccessToken".equals(claims.get("token_type"))
                    && claims.getSubject().equals(user.getUsername());


        } catch (JwtException ex) {
            throw new IllegalArgumentException("Token validation failed", ex);
        }
    }

}




