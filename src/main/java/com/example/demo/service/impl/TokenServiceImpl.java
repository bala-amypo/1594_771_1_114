package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;
import java.util.List;

public class TokenServiceImpl {

    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(TokenRepository t, ServiceCounterRepository c,
                            TokenLogRepository l, QueuePositionRepository q) {
        tokenRepo = t; counterRepo = c; logRepo = l; queueRepo = q;
    }

    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepo.findById(counterId)
                .orElseThrow(() -> new RuntimeException("not found"));

        if (!counter.getIsActive()) throw new IllegalArgumentException("not active");

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setTokenNumber(counter.getCounterName() + "-" + System.nanoTime());
        tokenRepo.save(token);

        int pos = tokenRepo
                .findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING")
                .size();

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(pos);
        queueRepo.save(qp);

        logRepo.save(new TokenLog(token, "Issued"));
        return token;
    }

    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("not found"));

        String cur = token.getStatus();
        if (status.equals("CANCELLED") ||
            (cur.equals("WAITING") && status.equals("SERVING")) ||
            (cur.equals("SERVING") && status.equals("COMPLETED"))) {

            token.setStatus(status);
            token.setCompletedAt(LocalDateTime.now());
            tokenRepo.save(token);
            logRepo.save(new TokenLog(token, status));
            return token;
        }
        throw new IllegalArgumentException("Invalid status");
    }

    public Token getToken(Long id) {
        return tokenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
