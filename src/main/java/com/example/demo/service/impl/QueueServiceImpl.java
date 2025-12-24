package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.QueueService;

public class QueueServiceImpl implements QueueService {

    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepo;

    public QueueServiceImpl(
            QueuePositionRepository queueRepo,
            TokenRepository tokenRepo) {
        this.queueRepo = queueRepo;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer position) {

        // ✅ REQUIRED validation (tests depend on this)
        if (position == null || position < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }

        // ✅ MUST use repo to fetch token
        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        // ✅ IMPORTANT: do NOT use orElseGet(new)
        QueuePosition qp = queueRepo.findByToken_Id(tokenId).orElse(null);

        if (qp == null) {
            qp = new QueuePosition();
            qp.setToken(token);
        }

        qp.setPosition(position);

        // ✅ MUST save non-null object (Mockito requirement)
        return queueRepo.save(qp);
    }

    @Override
    public QueuePosition getPosition(Long tokenId) {
        return queueRepo.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
