package com.example.demo.controller;

import com.example.demo.entity.TokenLog;
import com.example.demo.service.TokenLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class TokenLogController {

    private final TokenLogService logService;

    public TokenLogController(TokenLogService logService) {
        this.logService = logService;
    }

    @PostMapping("/{tokenId}")
    public TokenLog add(@PathVariable Long tokenId,
                        @RequestParam String message) {
        return logService.addLog(tokenId, message);
    }

    @GetMapping("/{tokenId}")
    public List<TokenLog> logs(@PathVariable Long tokenId) {
        return logService.getLogs(tokenId);
    }
}
