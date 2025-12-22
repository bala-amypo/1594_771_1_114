package com.example.demo.service.impl;

import com.example.demo.entity.Token;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public Token issueToken(Long counterId) {
        return null; // temporary for compilation
    }

    @Override
    public Token getToken(Long tokenId) {
        return null;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        return null;
    }
}
