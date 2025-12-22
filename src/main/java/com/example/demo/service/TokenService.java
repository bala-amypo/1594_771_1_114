package com.example.demo.service;

import com.example.demo.entity.Token;

public interface TokenService {

    Token issueToken(Long counterId);

    Token getToken(Long tokenId);

    Token updateStatus(Long tokenId, String status);
}
