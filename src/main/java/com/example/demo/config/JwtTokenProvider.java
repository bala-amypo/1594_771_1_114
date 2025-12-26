package com.example.demo.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long validityInMs;

    // Used by Spring
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.validity}") long validity) {

        this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validityInMs = validity;
    }

    // USED BY TEST CASES (VERY IMPORTANT)
    public JwtTokenProvider(String secret, long validity) {
        this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validityInMs = validity;
    }

    public String generateToken(Long userId, String email, String role) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("email", email);
        claims.put("role", role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
