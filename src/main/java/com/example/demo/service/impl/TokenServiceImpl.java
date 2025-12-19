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
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("not active");
        }

        Token token = new Token();
        token.setTokenNumber(UUID.randomUUID().toString());
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());

        Token saved = tokenRepository.save(token);

        queueRepository.save(new QueuePosition(saved, 1, LocalDateTime.now()));
        logRepository.save(new TokenLog(saved, "Token issued", null));

        return saved;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        token.setStatus(status);
        if ("COMPLETED".equals(status)) {
            token.setCompletedAt(LocalDateTime.now());
        }

        logRepository.save(new TokenLog(token, "Status updated", null));
        return tokenRepository.save(token);
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
    }
}
