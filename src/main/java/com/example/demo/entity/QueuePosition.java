package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "queue_positions")
public class QueuePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "token_id")
    private Token token;
    
    private Integer position;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Token getToken() { return token; }
    public void setToken(Token token) { this.token = token; }
    
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
