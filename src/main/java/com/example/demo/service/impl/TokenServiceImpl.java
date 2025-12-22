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
    private final QueuePositionRepository queueRepository;
    private final TokenLogRepository logRepository;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository counterRepository,
                            QueuePositionRepository queueRepository,
                            TokenLogRepository logRepository) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.queueRepository = queueRepository;
        this.logRepository = logRepository;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();
        token.setTokenNumber(UUID.randomUUID().toString());
        token.setStatus("WAITING");
        token.setServiceCounter(counter);
        token.setIssuedAt(LocalDateTime.now());

        tokenRepository.save(token);

        QueuePosition queuePosition = new QueuePosition(token, 1);
        queueRepository.save(queuePosition);

        logRepository.save(new TokenLog(token, "Token issued"));

        return token;
    }

    @Override
    public Token updateTokenStatus(Long tokenId, String status) {
        Token token = getTokenById(tokenId);

        String current = token.getStatus();

        if (current.equals("COMPLETED") || current.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        if (current.equals("WAITING") && !status.equals("SERVING") && !status.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        if (current.equals("SERVING") && !status.equals("COMPLETED") && !status.equals("CANCELLED")) {
            throw new IllegalArgumentException("Invalid status transition");
        }

        token.setStatus(status);

        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        tokenRepository.save(token);
        logRepository.save(new TokenLog(token, "Status changed to " + status));

        return token;
    }

    @Override
    public Token getTokenById(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
