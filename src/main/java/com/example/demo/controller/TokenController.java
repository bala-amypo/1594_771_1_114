package com.example.demo.controller;

import com.example.demo.service.TokenService;
import com.example.demo.entity.Token;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/tokens")
@Tag(name = "Tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue new token")
    public Token issueToken(@PathVariable Long counterId) {
        return tokenService.issueToken(counterId);
    }
}
