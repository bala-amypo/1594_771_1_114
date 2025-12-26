package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;

public class QueueServiceImpl {

    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepo;

    public QueueServiceImpl(QueuePositionRepository q, TokenRepository t) {
        queueRepo = q; tokenRepo = t;
    }

    public QueuePosition updateQueuePosition(Long tokenId, Integer pos) {
        if (pos < 1) throw new IllegalArgumentException(">= 1");

        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("not found"));

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(pos);
        return queueRepo.save(qp);
    }

    public QueuePosition getPosition(Long tokenId) {
        return queueRepo.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
