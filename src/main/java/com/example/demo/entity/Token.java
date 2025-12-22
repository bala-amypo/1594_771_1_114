package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tokenNumber;

    @ManyToOne
    private ServiceCounter serviceCounter;

    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime completedAt;

    public Token() {
    }

    public Token(String tokenNumber, ServiceCounter serviceCounter, String status) {
        this.tokenNumber = tokenNumber;
        this.serviceCounter = serviceCounter;
        this.status = status;
        this.issuedAt = LocalDateTime.now();
    }

    // getters & setters
}
