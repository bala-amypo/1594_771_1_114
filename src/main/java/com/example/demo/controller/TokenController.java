package com.example.demo.controller;

import com.example.demo.entity.Token;
import com.example.demo.entity.TokenStatus;
import com.example.demo.service.TokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/issue/{counterId}")
    public Token issueToken(@PathVariable Long counterId) {
        return tokenService.issueToken(counterId);
    }

    @PutMapping("/status/{tokenId}")
    public Token updateStatus(@PathVariable Long tokenId,
                              @RequestParam String status) {

        TokenStatus tokenStatus;
        try {
            tokenStatus = TokenStatus.valueOf(status);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status");
        }

        return tokenService.updateStatus(tokenId, tokenStatus);
    }

    @GetMapping("/{tokenId}")
    public Token getToken(@PathVariable Long tokenId) {
        return tokenService.getToken(tokenId);
    }
}
