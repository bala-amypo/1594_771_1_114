package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl {

    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(
            TokenRepository tokenRepo,
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
                .orElseThrow(() -> new RuntimeException("Counter not found"));

        if (!counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token token = new Token();   // MUST create
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token.setTokenNumber(counter.getCounterName() + "-" + System.nanoTime());

        token = tokenRepo.save(token); // MUST capture return

        List<Token> waiting =
                tokenRepo.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(
                        counterId, "WAITING");

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(waiting.size() + 1);
        queueRepo.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage("Token issued");
        logRepo.save(log);

        return token;
    }

    public Token updateStatus(Long tokenId, String status) {

        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (token.getStatus().equals("WAITING") && status.equals("SERVING")
                || token.getStatus().equals("SERVING")
                && (status.equals("COMPLETED") || status.equals("CANCELLED"))) {

            token.setStatus(status);

            if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
                token.setCompletedAt(LocalDateTime.now());
            }

            tokenRepo.save(token);

            TokenLog log = new TokenLog();
            log.setToken(token);
            log.setMessage("Status changed to " + status);
            logRepo.save(log);

            return token;
        }

        throw new IllegalArgumentException("Invalid status transition");
    }

    public Token getToken(Long id) {
        return tokenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
