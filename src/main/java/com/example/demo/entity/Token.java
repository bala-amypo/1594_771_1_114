package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tokenNumber;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "counter_id", nullable = false)
    @JsonIgnore   
    private ServiceCounter serviceCounter;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime completedAt;

    @OneToOne(mappedBy = "token", cascade = CascadeType.ALL)
    private QueuePosition queuePosition;

    @OneToMany(mappedBy = "token", cascade = CascadeType.ALL)
    private List<TokenLog> logs;

    public Token() {}

    public Token(String tokenNumber, String status, ServiceCounter serviceCounter) {
        this.tokenNumber = tokenNumber;
        this.status = status;
        this.serviceCounter = serviceCounter;
        this.issuedAt = LocalDateTime.now();
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceCounter getServiceCounter() {
        return serviceCounter;
    }

    public void setServiceCounter(ServiceCounter serviceCounter) {
        this.serviceCounter = serviceCounter;
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

    public QueuePosition getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(QueuePosition queuePosition) {
        this.queuePosition = queuePosition;
    }

    public List<TokenLog> getLogs() {
        return logs;
    }

    public void setLogs(List<TokenLog> logs) {
        this.logs = logs;
    }
}
