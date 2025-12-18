package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(TokenRepository tokenRepo,
                            ServiceCounterRepository counterRepo,
                            TokenLogRepository logRepo,
                            QueuePositionRepository queueRepo) {
        this.tokenRepo = tokenRepo;
        this.counterRepo = counterRepo;
        this.logRepo = logRepo;
        this.queueRepo = queueRepo;
    }

    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepo.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("not active");
        }

        Token token = new Token();
        token.setTokenNumber(UUID.randomUUID().toString());
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());

        Token saved = tokenRepo.save(token);

        queueRepo.save(new QueuePosition(saved, 1, LocalDateTime.now()));
        logRepo.save(new TokenLog(saved, "Token issued", null));

        return saved;
    }

    public Token updateStatus(Long tokenId, String status) {
        Token token = getToken(tokenId);

        if (!token.getStatus().equals("WAITING") && status.equals("SERVING") == false &&
            !token.getStatus().equals("SERVING") && status.equals("COMPLETED") == false) {
            throw new IllegalArgumentException("Invalid status");
        }

        token.setStatus(status);
        if (status.equals("COMPLETED")) {
            token.setCompletedAt(LocalDateTime.now());
        }

        logRepo.save(new TokenLog(token, "Status changed to " + status, null));
        return tokenRepo.save(token);
    }

    public Token getToken(Long tokenId) {
        return tokenRepo.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
