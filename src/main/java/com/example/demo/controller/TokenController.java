package com.example.demo.controller;

import com.example.demo.entity.Token;
import com.example.demo.service.impl.TokenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tokens")
@Tag(name = "Token Management", description = "Endpoints for issuing and status updates")
public class TokenController {
    private final TokenServiceImpl tokenService;

    public TokenController(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue a new token for a counter")
    public Token issue(@PathVariable Long counterId) {
        return tokenService.issueToken(counterId);
    }

    @PutMapping("/status/{tokenId}")
    @Operation(summary = "Update token status")
    public Token updateStatus(@PathVariable Long tokenId, @RequestParam String status) {
        return tokenService.updateStatus(tokenId, status);
    }

    @getMapping("/{tokenId}")
    @Operation(summary = "Get token details")
    public Token get(@PathVariable Long tokenId) {
        return tokenService.getToken(tokenId);
    }
}