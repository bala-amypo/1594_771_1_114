package com.example.demo.service.impl;

import com.example.demo.entity.TokenLog;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.service.TokenLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenLogServiceImpl implements TokenLogService {

    private final TokenLogRepository repository;

    public TokenLogServiceImpl(TokenLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public TokenLog addLog(TokenLog log) {

        if (log == null) {
            log = new TokenLog();
        }

        if (log.getCreatedAt() == null) {
            log.setCreatedAt(LocalDateTime.now());
        }

        TokenLog saved = repository.save(log);
        return saved;
    }
}
