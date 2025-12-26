package com.example.demo.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private final Key key;
    private final long validityInMillis;

    // ⚠️ EXACT constructor used in tests
    public JwtTokenProvider(String secretKey, long validityInMillis) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.validityInMillis = validityInMillis;
    }

    public String generateToken(Long userId, String email, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId)) // ⚠️ test checks subject
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
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
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
