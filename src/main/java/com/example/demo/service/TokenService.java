package com.example.demo.service;

import com.example.demo.entity.Token;

public interface TokenService {

    Token issueToken(Long counterId);

    Token updateTokenStatus(Long tokenId, String status);

    Token getTokenById(Long tokenId);
}
