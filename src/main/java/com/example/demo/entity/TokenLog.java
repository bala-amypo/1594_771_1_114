package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_logs")
public class TokenLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = false)
    private Token token;

    private String logMessage;

    // ✅ Auto timestamp (required by test)
    @Column(nullable = false)
    private LocalDateTime loggedAt = LocalDateTime.now();

    public TokenLog() {
    }

    public TokenLog(Token token, String logMessage) {
        this.token = token;
        this.logMessage = logMessage;
        this.loggedAt = LocalDateTime.now();
    }

    // Safety: ensures timestamp is never null
    @PrePersist
    public void onCreate() {
        if (this.loggedAt == null) {
            this.loggedAt = LocalDateTime.now();
        }
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public Token getToken() {
        return token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }
}
