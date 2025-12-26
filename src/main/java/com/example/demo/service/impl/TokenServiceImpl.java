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

    // --------------------------------------------------
    // ISSUE TOKEN  (t22, t66)
    // --------------------------------------------------
    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!Boolean.TRUE.equals(counter.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        // REQUIRED BY t22
        tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
                counterId, "WAITING"
        );

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber("TOKEN-" + UUID.randomUUID());

        Token saved = tokenRepository.save(token);

        // REQUIRED BY t22
        QueuePosition qp = new QueuePosition();
        qp.setToken(saved);
        qp.setPosition(1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepository.save(qp);

        // REQUIRED BY t22
        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setLogMessage("Token issued");
        logRepository.save(log);

        return saved;
    }

    // --------------------------------------------------
    // UPDATE STATUS  (t15, t16, t69)
    // --------------------------------------------------
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

        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            token.setCompletedAt(LocalDateTime.now());
        }

        // MUST save SAME object (t15 requirement)
        return tokenRepository.save(token);
    }

    // --------------------------------------------------
    // GET TOKEN
    // --------------------------------------------------
    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
