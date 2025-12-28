package com.example.demo.controller;

import com.example.demo.entity.TokenLog;
import com.example.demo.service.TokenLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@Tag(name = "Token Logs", description = "Token logging and audit trail")
@SecurityRequirement(name = "bearerAuth")
public class TokenLogController {
    private final TokenLogService logService;

    public TokenLogController(TokenLogService logService) {
        this.logService = logService;
    }

    @PostMapping("/{tokenId}")
    @Operation(summary = "Add log entry", description = "Add a new log entry for a token")
    @ApiResponse(responseCode = "200", description = "Log entry added successfully")
    public TokenLog addLog(@PathVariable Long tokenId, @RequestBody Map<String, String> request) {
        return logService.addLog(tokenId, request.get("message"));
    }

    @GetMapping("/{tokenId}")
    @Operation(summary = "Get token logs", description = "Retrieve all log entries for a token")
    @ApiResponse(responseCode = "200", description = "Token logs retrieved successfully")
    public List<TokenLog> getLogs(@PathVariable Long tokenId) {
        return logService.getLogs(tokenId);
    }
}