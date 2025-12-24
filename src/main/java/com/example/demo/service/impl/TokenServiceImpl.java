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
    private final TokenLogRepository tokenLogRepository;
    private final QueuePositionRepository queuePositionRepository;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository counterRepository,
                            TokenLogRepository tokenLogRepository,
                            QueuePositionRepository queuePositionRepository) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.tokenLogRepository = tokenLogRepository;
        this.queuePositionRepository = queuePositionRepository;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        String tokenNumber = UUID.randomUUID().toString();

        Token token = new Token(
                tokenNumber,
                counter,
                "WAITING",
                LocalDateTime.now(),
                null
        );

        Token savedToken = tokenRepository.save(token);

        QueuePosition position = new QueuePosition(savedToken, 1, LocalDateTime.now());
        queuePositionRepository.save(position);

        TokenLog log = new TokenLog(savedToken, "Token issued", null);
        tokenLogRepository.save(log);

        return savedToken;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        String current = token.getStatus();

        if (current.equals("COMPLETED") || current.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status");
        }

        if (!status.equals("SERVING") &&
            !status.equals("COMPLETED") &&
            !status.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status");
        }

        token.setStatus(status);

        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        Token updated = tokenRepository.save(token);

        tokenLogRepository.save(
                new TokenLog(updated, "Status changed to " + status, null)
        );

        return updated;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
