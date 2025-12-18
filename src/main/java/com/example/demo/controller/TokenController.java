package com.example.demo.controller;

import com.example.demo.entity.Token;
import com.example.demo.service.TokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenService service;

    public TokenController(TokenService service) {
        this.service = service;
    }

    @PostMapping("/issue/{counterId}")
    public Token issue(@PathVariable Long counterId) {
        return service.issueToken(counterId);
    }

    @PutMapping("/status/{tokenId}")
    public Token update(@PathVariable Long tokenId,
                        @RequestParam String status) {
        return service.updateStatus(tokenId, status);
    }

    @GetMapping("/{tokenId}")
    public Token get(@PathVariable Long tokenId) {
        return service.getToken(tokenId);
    }
}
