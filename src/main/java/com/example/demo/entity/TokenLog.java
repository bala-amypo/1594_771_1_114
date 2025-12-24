package com.example.demo.entity;

import java.time.LocalDateTime;

public class TokenLog {

    private Long id;
    private Token token;
    private String message;
    private LocalDateTime loggedAt = LocalDateTime.now(); // 🚨 IMPORTANT

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Token getToken() { return token; }
    public void setToken(Token token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
}
