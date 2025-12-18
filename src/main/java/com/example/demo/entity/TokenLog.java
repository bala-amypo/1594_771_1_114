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

    @PrePersist
    void onCreate() {
        this.loggedAt = LocalDateTime.now();
    }

    public TokenLog() {}

    // getters & setters
}
