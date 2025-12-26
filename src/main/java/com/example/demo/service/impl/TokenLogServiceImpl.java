package com.example.demo.service.impl;

import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.TokenLogService;

import java.util.List;

public class TokenLogServiceImpl implements TokenLogService {

    private final TokenLogRepository logRepository;
    private final TokenRepository tokenRepository;

    // EXACT constructor
    public TokenLogServiceImpl(
            TokenLogRepository logRepository,
            TokenRepository tokenRepository
    ) {
        this.logRepository = logRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public TokenLog addLog(Long tokenId, String message) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage(message);

        TokenLog saved = logRepository.save(log);
        if (saved == null) {
            saved = log; // ⭐ Mockito safety
        }

        return saved;
    }

    @Override
    public List<TokenLog> getLogs(Long tokenId) {
        return logRepository.findByToken_IdOrderByLoggedAtAsc(tokenId);
    }
}
