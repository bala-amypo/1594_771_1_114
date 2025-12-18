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

    public Token() {}

    public Long getId() { return id; }
    public String getTokenNumber() { return tokenNumber; }
    public ServiceCounter getServiceCounter() { return serviceCounter; }
    public String getStatus() { return status; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setId(Long id) { this.id = id; }
    public void setTokenNumber(String tokenNumber) { this.tokenNumber = tokenNumber; }
    public void setServiceCounter(ServiceCounter serviceCounter) { this.serviceCounter = serviceCounter; }
    public void setStatus(String status) { this.status = status; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
