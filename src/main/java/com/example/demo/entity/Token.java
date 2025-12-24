package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "tokens",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "tokenNumber")
    }
)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenNumber;

    @ManyToOne
    @JoinColumn(name = "service_counter_id")
    private ServiceCounter serviceCounter;

    // ✅ MUST be enum (tests expect this)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.WAITING;

    // ✅ Auto issue time
    @Column(nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    // ✅ Required by tests
    private LocalDateTime completedAt;

    // ✅ Required by cancellation tests
    private LocalDateTime cancelledAt;

    public Token() {
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public ServiceCounter getServiceCounter() {
        return serviceCounter;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public void setServiceCounter(ServiceCounter serviceCounter) {
        this.serviceCounter = serviceCounter;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
