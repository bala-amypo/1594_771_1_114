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
    private final TokenLogRepository tokenLogRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(
            TokenRepository tokenRepository,
            ServiceCounterRepository counterRepository,
            TokenLogRepository tokenLogRepository,
            QueuePositionRepository queueRepository
    ) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.tokenLogRepository = tokenLogRepository;
        this.queueRepository = queueRepository;
    }

    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Counter not found"));

        if (!Boolean.TRUE.equals(counter.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();
        token.setTokenNumber(UUID.randomUUID().toString());
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());

        token = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepository.save(qp);

        tokenLogRepository.save(
                new TokenLog(null, token, "Token issued", LocalDateTime.now())
        );

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Token not found"));

        String current = token.getStatus();

        if (current.equals("COMPLETED") || current.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        token.setStatus(status);
        token = tokenRepository.save(token);

        tokenLogRepository.save(
                new TokenLog(null, token,
                        "Status changed to " + status,
                        LocalDateTime.now())
        );

        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Token not found"));
    }
}
