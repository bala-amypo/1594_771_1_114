package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;

import java.time.LocalDateTime;
import java.util.List;

public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(
            TokenRepository tokenRepository,
            ServiceCounterRepository counterRepository,
            TokenLogRepository logRepository,
            QueuePositionRepository queueRepository) {

        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepository = logRepository;
        this.queueRepository = queueRepository;
    }

    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (!Boolean.TRUE.equals(counter.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());

        // 🔥 SAVE FIRST
        token = tokenRepository.save(token);

        // 🔥 COUNT AFTER SAVE
        List<Token> waiting =
                tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
                        counterId, "WAITING");

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(waiting.size());
        queueRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage("Token issued");
        logRepository.save(log);

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if ("WAITING".equals(token.getStatus()) && "SERVING".equals(status)) {

            token.setStatus("SERVING");
            token = tokenRepository.save(token);

        } else if ("SERVING".equals(token.getStatus()) &&
                ("COMPLETED".equals(status) || "CANCELLED".equals(status))) {

            token.setStatus(status);
            token.setCompletedAt(LocalDateTime.now());
            token = tokenRepository.save(token);

            queueRepository.findByToken_Id(tokenId)
                    .ifPresent(queueRepository::delete);

        } else {
            throw new IllegalArgumentException("Invalid status transition");
        }

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage("Status changed to " + status);
        logRepository.save(log);

        return token;
    }

    @Override
    public Token getToken(Long id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
