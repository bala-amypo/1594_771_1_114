package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
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

    public TokenServiceImpl(TokenRepository tokenRepository,
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
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setTokenNumber(UUID.randomUUID().toString());
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());

        // STEP 4 FIX
        Token savedToken = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        qp.setPosition(1);
        queueRepository.save(qp);

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

        String oldStatus = token.getStatus();

        // STEP 5 – status validation
        if (oldStatus.equals("WAITING") && status.equals("COMPLETED")) {
            throw new IllegalStateException("Invalid status");
        }
        if (oldStatus.equals("SERVING") && status.equals("WAITING")) {
            throw new IllegalStateException("Invalid status");
        }

        token.setStatus(status);

        if (status.equals("COMPLETED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        Token updatedToken = tokenRepository.save(token);
        return updatedToken;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
