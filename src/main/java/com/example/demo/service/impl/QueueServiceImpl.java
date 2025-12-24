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

        // ✅ validation
        if (position == null || position < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }

        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        // ✅ IMPORTANT: reuse existing QueuePosition if present
        QueuePosition qp = queueRepo.findByToken_Id(tokenId)
                .orElseGet(QueuePosition::new);

        qp.setToken(token);
        qp.setPosition(position);

        return queueRepo.save(qp);
    }

    @Override
    public QueuePosition getPosition(Long tokenId) {
        return queueRepo.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
