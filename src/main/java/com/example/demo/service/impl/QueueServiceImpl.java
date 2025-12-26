package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl {

    private final QueuePositionRepository repo;
    private final TokenRepository tokenRepo;

    public QueueServiceImpl(QueuePositionRepository repo, TokenRepository tokenRepo) {
        this.repo = repo;
        this.tokenRepo = tokenRepo;
    }

    public QueuePosition updateQueuePosition(Long tokenId, int position) {

        if (position < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }

        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(position);

        return repo.save(qp); // SAME object
    }

    public QueuePosition getPosition(Long tokenId) {
        return repo.findByToken_Id(tokenId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }
}
