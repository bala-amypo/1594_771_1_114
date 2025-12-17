package com.example.demo.entity;
import java.time.LocalDateTime;

public class TokenLog{
    private Long id;
    private Token token;
    private String logMessage;
    private LocalDateTime loggedAt;
    
    public void onCreate(){
        loggedAt = LocalDateTime.now();
    }
}