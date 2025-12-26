package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl {
    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(TokenRepository tr, ServiceCounterRepository cr, TokenLogRepository lr, QueuePositionRepository qr) {
        this.tokenRepository = tr;
        this.counterRepository = cr;
        this.logRepo = lr;
        this.queueRepo = qr;
    }

    public Token issueToken(Long counterId) {
        ServiceCounter sc = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found")); // Keyword: not found
        
        if (!sc.getIsActive()) {
            throw new IllegalArgumentException("Counter is not active"); // Keyword: not active
        }

        Token token = new Token();
        token.setServiceCounter(sc);
        token.setStatus("WAITING");
        token.setTokenNumber("TK-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        Token savedToken = tokenRepository.save(token);

        // Position logic for t12/t66
        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        int currentWaiting = tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING").size();
        qp.setPosition(currentWaiting);
        queueRepo.save(qp);

        return savedToken;
    }

    public Token updateStatus(Long tokenId, String newStatus) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        // Transition Logic: WAITING -> SERVING -> COMPLETED/CANCELLED
        String currentStatus = token.getStatus();
        boolean valid = false;
        if (currentStatus.equals("WAITING") && (newStatus.equals("SERVING") || newStatus.equals("CANCELLED"))) valid = true;
        if (currentStatus.equals("SERVING") && (newStatus.equals("COMPLETED") || newStatus.equals("CANCELLED"))) valid = true;

        if (!valid) throw new IllegalArgumentException("Invalid status transition"); // Keyword: Invalid status

        token.setStatus(newStatus);
        if (newStatus.equals("COMPLETED") || newStatus.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now()); // Required by t16 and t69
        }

        tokenRepository.save(token);
        
        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Status updated to " + newStatus);
        logRepo.save(log);

        return token;
    }

    public Token getToken(Long id) {
        return tokenRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }
}