package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tokenNumber;

    @ManyToOne
    @JoinColumn(name = "counter_id")
    private ServiceCounter serviceCounter;

    private String status; // WAITING, SERVING, COMPLETED, CANCELLED

    private LocalDateTime issuedAt = LocalDateTime.now();
    
    private LocalDateTime completedAt;
}