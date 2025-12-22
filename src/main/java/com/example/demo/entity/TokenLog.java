package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TokenLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Token token;

    private String logMessage;
    private LocalDateTime loggedAt;

    public TokenLog() {
    }

    public TokenLog(Token token, String logMessage) {
        this.token = token;
        this.logMessage = logMessage;
    }

    @PrePersist
    public void onCreate() {
        this.loggedAt = LocalDateTime.now();
    }

    // getters & setters
}
