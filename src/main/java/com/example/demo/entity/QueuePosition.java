package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class QueuePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Token token;

    private Integer position;
    private LocalDateTime updatedAt;

    public QueuePosition() {
    }

    public QueuePosition(Token token, Integer position, LocalDateTime updatedAt) {
        this.token = token;
        this.position = position;
        this.updatedAt = updatedAt;
    }

    // getters & setters
}
