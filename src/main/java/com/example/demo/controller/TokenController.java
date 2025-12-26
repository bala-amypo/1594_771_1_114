package com.example.demo.controller;

import com.example.demo.entity.Token;
import com.example.demo.service.impl.TokenServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenServiceImpl service;

    public TokenController(TokenServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/issue/{counterId}")
    public Token issueToken(@PathVariable Long counterId) {
        return service.issueToken(counterId);
    }

    @PostMapping("/status/{tokenId}")
    public Token updateStatus(
            @PathVariable Long tokenId,
            @RequestParam String status) {
        return service.updateStatus(tokenId, status);
    }

    @GetMapping("/{tokenId}")
    public Token getToken(@PathVariable Long tokenId) {
        return service.getToken(tokenId);
    }
}
