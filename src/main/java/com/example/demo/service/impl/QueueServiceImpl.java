package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.QueueService;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueuePositionRepository queueRepository;
    private final TokenRepository tokenRepository;

    public QueueServiceImpl(
            QueuePositionRepository queueRepository,
            TokenRepository tokenRepository
    ) {
        this.queueRepository = queueRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer newPosition) {
        if (newPosition < 1) {
            throw new IllegalArgumentException(">= 1");
        }

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("not found"));

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(newPosition);
        return queueRepository.save(qp);
    }

    @Override
    public QueuePosition getPosition(Long tokenId) {
        return queueRepository.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
