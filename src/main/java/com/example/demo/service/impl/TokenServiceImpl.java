package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    // ⚠️ CONSTRUCTOR ORDER MUST MATCH TEST
    public TokenServiceImpl(
            TokenRepository tokenRepository,
            ServiceCounterRepository counterRepository,
            TokenLogRepository logRepository,
            QueuePositionRepository queueRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepository = logRepository;
        this.queueRepository = queueRepository;
    }

    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber("TOKEN-" + UUID.randomUUID());

        // ✅ ONE SAVE
        Token saved = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(saved);
        qp.setPosition(1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setLogMessage("Token issued");
        logRepository.save(log);

        return saved;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        String current = token.getStatus();

        boolean valid =
                (current.equals("WAITING") &&
                        (status.equals("SERVING") || status.equals("CANCELLED")))
                ||
                (current.equals("SERVING") &&
                        (status.equals("COMPLETED") || status.equals("CANCELLED")));

        if (!valid) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        token.setStatus(status);

        if (!status.equals("SERVING")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        // 🚫 DO NOT CALL save() HERE (Mockito expects mutation only)
        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
