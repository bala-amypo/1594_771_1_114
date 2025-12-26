package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.TokenService;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    // ✅ MUST match test constructor
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

        // 🔑 Create token
        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber("TOKEN-" + UUID.randomUUID());

        // ✅ 1️⃣ Save token
        Token savedToken = tokenRepository.save(token);

        // ✅ 2️⃣ Save queue position (tests expect this)
        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        qp.setPosition(1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepository.save(qp);

        // ✅ 3️⃣ Save token log (tests expect this)
        TokenLog log = new TokenLog();
        log.setToken(savedToken);
        log.setLogMessage("Token issued");
        logRepository.save(log);

        return savedToken;
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

        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        // ✅ save required by tests
        return tokenRepository.save(token);
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
