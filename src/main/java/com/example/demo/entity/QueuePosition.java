package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_positions")
public class QueuePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One token → one queue position
    @OneToOne
    @JoinColumn(name = "token_id", nullable = false, unique = true)
    private Token token;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ✅ Required by JPA
    public QueuePosition() {
    }

    // ✅ Required by services & tests
    public QueuePosition(Token token, Integer position, LocalDateTime updatedAt) {
        this.token = token;
        this.position = position;
        this.updatedAt = updatedAt;
    }

    // ✅ Auto timestamp (required by tests)
    @PrePersist
    @PreUpdate
    public void onUpdate() {
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public Token getToken() {
        return token;
    }

    public Integer getPosition() {
        return position;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
