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

        Token savedToken = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(savedToken);
        qp.setPosition(1);
        queueRepository.save(qp);

        // ✅ TokenLog (NO logMessage)
        TokenLog log = new TokenLog();
        log.setToken(savedToken);
        log.setStatus(savedToken.getStatus());
        log.setLoggedAt(LocalDateTime.now());
        logRepository.save(log);

        return savedToken;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        token.setStatus(status);
        Token updated = tokenRepository.save(token);

        // ✅ Status change ku log create pannanum
        TokenLog log = new TokenLog();
        log.setToken(updated);
        log.setStatus(status);
        log.setLoggedAt(LocalDateTime.now());
        logRepository.save(log);

        return updated;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
