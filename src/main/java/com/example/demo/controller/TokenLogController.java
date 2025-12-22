package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/logs")
@Tag(name = "Token Logs")
public class TokenLogController {

    @PostMapping("/{tokenId}")
    @Operation(summary = "Add token log")
    public String addLog(@PathVariable Long tokenId) {
        return "Log added";
    }

    @GetMapping("/{tokenId}")
    @Operation(summary = "Get token logs")
    public String getLogs(@PathVariable Long tokenId) {
        return "Logs list";
    }
}
