package com.example.demo.controller;

import com.example.demo.entity.TokenLog;
import com.example.demo.service.impl.TokenLogServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class TokenLogController {

    private final TokenLogServiceImpl service;

    public TokenLogController(TokenLogServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{tokenId}")
    public TokenLog addLog(
            @PathVariable Long tokenId,
            @RequestParam String message) {
        return service.addLog(tokenId, message);
    }

    @GetMapping("/{tokenId}")
    public List<TokenLog> getLogs(@PathVariable Long tokenId) {
        return service.getLogs(tokenId);
    }
}
