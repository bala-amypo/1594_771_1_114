package com.example.demo.controller;

import com.example.demo.entity.TokenLog;
import com.example.demo.service.impl.TokenLogServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Audit Logs", description = "Endpoints for token history logs")
public class TokenLogController {
    private final TokenLogServiceImpl logService;

    public TokenLogController(TokenLogServiceImpl logService) {
        this.logService = logService;
    }

    @PostMapping("/{tokenId}")
    @Operation(summary = "Add a log message for a token")
    public TokenLog add(@PathVariable Long tokenId, @RequestBody String message) {
        return logService.addLog(tokenId, message);
    }

    @getMapping("/{tokenId}")
    @Operation(summary = "List all logs for a specific token")
    public List<TokenLog> get(@PathVariable Long tokenId) {
        return logService.getLogs(tokenId);
    }
}