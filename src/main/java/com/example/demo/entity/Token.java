package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenNumber;

    @ManyToOne
    private ServiceCounter serviceCounter;

    private String status;

    private LocalDateTime issuedAt;
    private LocalDateTime completedAt;
}
