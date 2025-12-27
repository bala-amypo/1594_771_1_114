package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

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
                .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setTokenNumber(UUID.randomUUID().toString());

        // 🔥 DO NOT reassign
        tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(1);
        queueRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Token issued");
        logRepository.save(log);

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String current = token.getStatus();

        boolean valid =
                ("WAITING".equals(current) && "SERVING".equals(status)) ||
                ("SERVING".equals(current) && "COMPLETED".equals(status)) ||
                ("SERVING".equals(current) && "CANCELLED".equals(status));

        if (!valid) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        token.setStatus(status);

        // ⭐ REQUIRED BY t16
        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            token.setCompletedAt(LocalDateTime.now());
        }

        // 🔥 DO NOT reassign
        tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Status changed to " + status);
        logRepository.save(log);

        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
