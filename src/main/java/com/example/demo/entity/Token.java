package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String tokenNumber;
    
    private String status = "WAITING";
    
    @ManyToOne
    @JoinColumn(name = "service_counter_id")
    private ServiceCounter serviceCounter;
    
    private LocalDateTime issuedAt = LocalDateTime.now();
    private LocalDateTime completedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(String tokenNumber) { this.tokenNumber = tokenNumber; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public ServiceCounter getServiceCounter() { return serviceCounter; }
    public void setServiceCounter(ServiceCounter serviceCounter) { this.serviceCounter = serviceCounter; }
    
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
