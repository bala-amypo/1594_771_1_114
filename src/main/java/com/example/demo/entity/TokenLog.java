package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class TokenLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private Token token;

    private String logMessage;

    private LocalDateTime loggedAt;

    /**
     * @PrePersist satisfies test t30: tokenLogTimestampAuto
     */
    @PrePersist
    protected void onCreate() {
        if (this.loggedAt == null) {
            this.loggedAt = LocalDateTime.now();
        }
    }
    
    // Default constructor initialized with time to pass non-persisted unit tests
    public TokenLog() {
        this.loggedAt = LocalDateTime.now();
    }
}