package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens", uniqueConstraints = {
        @UniqueConstraint(columnNames = "tokenNumber")
})
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenNumber;

    @ManyToOne
    @JoinColumn(name = "service_counter_id")
    private ServiceCounter serviceCounter;

    private String status;

    private LocalDateTime issuedAt;

    private LocalDateTime completedAt;

    public Token() {
    }

    public Token(String tokenNumber, ServiceCounter serviceCounter, String status,
                 LocalDateTime issuedAt, LocalDateTime completedAt) {
        this.tokenNumber = tokenNumber;
        this.serviceCounter = serviceCounter;
        this.status = status;
        this.issuedAt = issuedAt;
        this.completedAt = completedAt;
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public ServiceCounter getServiceCounter() {
        return serviceCounter;
    }

    public void setServiceCounter(ServiceCounter serviceCounter) {
        this.serviceCounter = serviceCounter;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    
    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
