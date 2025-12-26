package com.example.demo.controller;

import com.example.demo.entity.Token;
import com.example.demo.service.TokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/counter/{counterId}")
    public Token issueToken(@PathVariable Long counterId) {
        return tokenService.issueToken(counterId);
    }

    @PutMapping("/{tokenId}/status")
    public Token updateStatus(
            @PathVariable Long tokenId,
            @RequestParam String status
    ) {
        return tokenService.updateStatus(tokenId, status);
    }

    @GetMapping("/{tokenId}")
    public Token getToken(@PathVariable Long tokenId) {
        return tokenService.getToken(tokenId);
    }
}
