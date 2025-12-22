package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tokens")
@Tag(name = "Tokens")
public class TokenController {

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue token")
    public String issueToken(@PathVariable Long counterId) {
        return "Token issued";
    }

    @PutMapping("/status/{tokenId}")
    @Operation(summary = "Update token status")
    public String updateStatus(
            @PathVariable Long tokenId,
            @RequestParam String status) {
        return "Status updated";
    }

    @GetMapping("/{tokenId}")
    @Operation(summary = "Get token details")
    public String getToken(@PathVariable Long tokenId) {
        return "Token details";
    }
}
