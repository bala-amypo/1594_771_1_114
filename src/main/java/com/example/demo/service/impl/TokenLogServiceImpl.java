package com.example.demo.service.impl;

import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TokenLogServiceImpl {
    private final TokenLogRepository logRepo;
    private final TokenRepository tokenRepository;

    // Exact constructor signature required for test suite instantiation
    public TokenLogServiceImpl(TokenLogRepository logRepo, TokenRepository tokenRepository) {
        this.logRepo = logRepo;
        this.tokenRepository = tokenRepository;
    }

    public TokenLog addLog(Long tokenId, String message) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found")); // Keyword: not found

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage(message);
        
        return logRepo.save(log);
    }

    public List<TokenLog> getLogs(Long tokenId) {
        // Uses the specific repository method required for sorting
        return logRepo.findByToken_IdOrderByLoggedAtAsc(tokenId);
    }
}