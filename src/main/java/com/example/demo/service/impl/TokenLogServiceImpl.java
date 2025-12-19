package com.example.demo.service.impl;

import com.example.demo.entity.TokenLog;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.service.TokenLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenLogServiceImpl implements TokenLogService {

    private final TokenLogRepository tokenLogRepository;

    public TokenLogServiceImpl(TokenLogRepository tokenLogRepository) {
        this.tokenLogRepository = tokenLogRepository;
    }

    @Override
    public TokenLog logStatus(Long tokenId, String status) {

        TokenLog log = new TokenLog();
        log.setStatus(status);              // ✅ IMPORTANT
        log.setLoggedAt(LocalDateTime.now());

        return tokenLogRepository.save(log);
    }

    @Override
    public List<TokenLog> getLogs(Long tokenId) {
        return tokenLogRepository.findByTokenIdOrderByLoggedAtAsc(tokenId);
    }
}
