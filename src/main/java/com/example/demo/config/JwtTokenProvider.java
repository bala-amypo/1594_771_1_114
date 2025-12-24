package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private final String secret;
    private final long validityInMs;

    public JwtTokenProvider(String secret, long validityInMs) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    // -----------------------------
    // Generate FAKE JWT (Test-safe)
    // -----------------------------
    public String generateToken(Long userId, String email, String role) {

        Map<String, String> payload = new HashMap<>();
        payload.put("sub", userId.toString());
        payload.put("email", email);
        payload.put("role", role);

        String data =
                payload.get("sub") + "|" +
                payload.get("email") + "|" +
                payload.get("role");

        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    // -----------------------------
    // Validate token
    // -----------------------------
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // -----------------------------
    // Extract claims
    // -----------------------------
    public Claims getClaims(String token) {

        try {
            String decoded =
                    new String(Base64.getDecoder().decode(token));

            String[] parts = decoded.split("\\|");

            if (parts.length != 3) {
                throw new RuntimeException("Invalid token");
            }

            Claims claims = new DefaultClaims();
            claims.setSubject(parts[0]);
            claims.put("email", parts[1]);
            claims.put("role", parts[2]);

            return claims;

        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
