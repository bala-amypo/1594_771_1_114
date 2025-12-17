package com.example.demo.entity;
import java.time.LocalDateTime;

public class Token{
    private Long id;
    private String tokenNumber;
    private ServiceCounter ServiceCounter;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime completedAt;
}