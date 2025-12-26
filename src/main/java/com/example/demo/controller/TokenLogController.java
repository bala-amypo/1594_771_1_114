package com.example.demo.controller;

import com.example.demo.entity.TokenLog;
import com.example.demo.service.TokenLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class TokenLogController {

    private final TokenLogService tokenLogService;

    public TokenLogController(TokenLogService tokenLogService) {
        this.tokenLogService = tokenLogService;
    }

    @PostMapping("/{tokenId}")
    public TokenLog addLog(
            @PathVariable Long tokenId,
            @RequestParam String message
    ) {
        return tokenLogService.addLog(tokenId, message);
    }

    @GetMapping("/{tokenId}")
    public List<TokenLog> getLogs(@PathVariable Long tokenId) {
        return tokenLogService.getLogs(tokenId);
    }
}
