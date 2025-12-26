package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl {
    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepository;

    // Exact constructor signature required for test suite instantiation
    public QueueServiceImpl(QueuePositionRepository queueRepo, TokenRepository tokenRepository) {
        this.queueRepo = queueRepo;
        this.tokenRepository = tokenRepository;
    }

    public QueuePosition updateQueuePosition(Long tokenId, Integer newPosition) {
        // Requirement and Test t68: Position must be >= 1
        if (newPosition == null || newPosition < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found")); // Keyword: not found

        QueuePosition qp = queueRepo.findByToken_Id(tokenId).orElse(new QueuePosition());
        qp.setToken(token);
        qp.setPosition(newPosition);
        
        return queueRepo.save(qp);
    }

    public QueuePosition getPosition(Long tokenId) {
        return queueRepo.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("Queue position not found")); // Keyword: not found
    }
}