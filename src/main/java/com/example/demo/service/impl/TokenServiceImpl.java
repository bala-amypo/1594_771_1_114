package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, ServiceCounterRepository counterRepository, 
                           TokenLogRepository logRepository, QueuePositionRepository queueRepository) {
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
            throw new IllegalArgumentException("Counter is not active");
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setTokenNumber(counter.getCounterName() + "-" + System.currentTimeMillis());
        token.setStatus("WAITING");
        token = tokenRepository.save(token);

        List<Token> waitingTokens = tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");
        
        QueuePosition queuePosition = new QueuePosition();
        queuePosition.setToken(token);
        queuePosition.setPosition(waitingTokens.size());
        queueRepository.save(queuePosition);

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

        if ("WAITING".equals(token.getStatus()) && "COMPLETED".equals(status)) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        token.setStatus(status);
        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            token.setCompletedAt(LocalDateTime.now());
        }
        
        token = tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage("Status updated to " + status);
        logRepository.save(log);

        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
