package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class QueuePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "token_id")
    private Token token;

    private Integer position;

    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Custom setter to pass test t31: positionValidation
     */
    public void setPosition(Integer position) {
        if (position != null && position < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }
        this.position = position;
    }
}