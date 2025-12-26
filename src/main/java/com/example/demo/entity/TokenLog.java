package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TokenLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private Token token;

    private String message;

    private LocalDateTime loggedAt = LocalDateTime.now();

    // -------- getters & setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    // ✅ REQUIRED BY SERVICE
    public String getMessage() {
        return message;
    }

    // ✅ REQUIRED BY SERVICE
    public void setMessage(String message) {
        this.message = message;
    }
}
