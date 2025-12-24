package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    // ================= ISSUE TOKEN =================
    @Override
    public Token issueToken(Long counterId) {

        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!Boolean.TRUE.equals(counter.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        String tokenNumber = UUID.randomUUID().toString();

        Token token = new Token();
        token.setTokenNumber(tokenNumber);
        token.setServiceCounter(counter);
        token.setStatus(TokenStatus.WAITING);

        Token savedToken = tokenRepository.save(token);

        // Queue position = next available
        int position = (int) queuePositionRepository.countWaitingTokens(counter.getId()) + 1;
        QueuePosition queuePosition = new QueuePosition(savedToken, position);
        queuePositionRepository.save(queuePosition);

        tokenLogRepository.save(
                new TokenLog(savedToken, "Token issued")
        );

        return savedToken;
    }

    // ================= UPDATE STATUS =================
    @Override
    public Token updateStatus(Long tokenId, TokenStatus newStatus) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        TokenStatus current = token.getStatus();

        // ❌ Final states cannot change
        if (current == TokenStatus.COMPLETED || current == TokenStatus.CANCELLED) {
            throw new IllegalStateException("Invalid status transition");
        }

        // WAITING → SERVING
        if (current == TokenStatus.WAITING && newStatus == TokenStatus.SERVING) {
            token.setStatus(TokenStatus.SERVING);
        }
        // SERVING → COMPLETED
        else if (current == TokenStatus.SERVING && newStatus == TokenStatus.COMPLETED) {
            token.setStatus(TokenStatus.COMPLETED);
            token.setCompletedAt(LocalDateTime.now());
        }
        // WAITING → CANCELLED
        else if (current == TokenStatus.WAITING && newStatus == TokenStatus.CANCELLED) {
            token.setStatus(TokenStatus.CANCELLED);
            token.setCancelledAt(LocalDateTime.now());
        }
        else {
            throw new IllegalStateException("Invalid status transition");
        }

        Token updated = tokenRepository.save(token);

        tokenLogRepository.save(
                new TokenLog(updated, "Status changed to " + updated.getStatus())
        );

        return updated;
    }

    // ================= GET TOKEN =================
    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
