package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TokenLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Token token;

    private String logMessage;
    private LocalDateTime loggedAt = LocalDateTime.now();

    public TokenLog() {}

    public TokenLog(Token token, String message) {
        this.token = token;
        this.logMessage = message;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Token getToken() { return token; }
    public void setToken(Token token) { this.token = token; }

    public String getLogMessage() { return logMessage; }
    public void setLogMessage(String logMessage) { this.logMessage = logMessage; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
}
