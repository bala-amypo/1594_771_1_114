package com.example.demo.service;

import com.example.demo.entity.Token;
import com.example.demo.entity.TokenStatus;

public interface TokenService {

    Token issueToken(Long counterId);

    // ✅ MUST use TokenStatus (tests expect enum)
    Token updateStatus(Long tokenId, TokenStatus status);

    Token getToken(Long tokenId);
}
