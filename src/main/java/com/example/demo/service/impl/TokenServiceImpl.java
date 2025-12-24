package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
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

        // ✅ CREATE token
        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());

        // ✅ MUST save & reuse returned object (Mockito relies on this)
        token = tokenRepository.save(token);

        // ✅ Queue position
        QueuePosition qp = new QueuePosition();
        qp.setToken(token);

        List<Token> waiting =
                tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
                        counterId, "WAITING");

        qp.setPosition(waiting.size() + 1);
        queueRepository.save(qp);

        // ✅ Log (DO NOT manually set loggedAt)
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

        } else if ("SERVING".equals(token.getStatus()) &&
                ("COMPLETED".equals(status) || "CANCELLED".equals(status))) {

            token.setStatus(status);
            token.setCompletedAt(LocalDateTime.now());

            // ✅ Remove from queue when finished
            queueRepository.findByToken_Id(tokenId)
                    .ifPresent(queueRepository::delete);

        } else {
            throw new IllegalArgumentException("Invalid status transition");
        }

        // ✅ MUST save token
        token = tokenRepository.save(token);

        // ✅ Log status change
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
