package com.example.demo.service.impl;

import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenLogServiceImpl {

    private final TokenLogRepository repo;
    private final TokenRepository tokenRepo;

    public TokenLogServiceImpl(TokenLogRepository repo, TokenRepository tokenRepo) {
        this.repo = repo;
        this.tokenRepo = tokenRepo;
    }

    public TokenLog addLog(Long tokenId, String message) {

        Token token = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setMessage(message);

        return repo.save(log); // SAME object
    }

    public List<TokenLog> getLogs(Long tokenId) {
        return repo.findByToken_IdOrderByLoggedAtAsc(tokenId);
    }
}
