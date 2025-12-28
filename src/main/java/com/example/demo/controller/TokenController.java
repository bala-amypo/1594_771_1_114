package com.example.demo.controller;

import com.example.demo.dto.StatusUpdateRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.entity.Token;
import com.example.demo.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@Tag(name = "Token Management", description = "Token issuing and status management")
@SecurityRequirement(name = "bearerAuth")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue new token", description = "Issue a new token for the specified counter")
    @ApiResponse(responseCode = "200", description = "Token issued successfully")
    public TokenResponse issueToken(@PathVariable Long counterId) {
        Token token = tokenService.issueToken(counterId);
        return mapToResponse(token);
    }

    @PutMapping("/status/{tokenId}")
    @Operation(summary = "Update token status", description = "Update the status of an existing token")
    @ApiResponse(responseCode = "200", description = "Token status updated successfully")
    public TokenResponse updateStatus(@PathVariable Long tokenId, @RequestBody StatusUpdateRequest request) {
        Token token = tokenService.updateStatus(tokenId, request.getStatus());
        return mapToResponse(token);
    }

    @GetMapping("/{tokenId}")
    @Operation(summary = "Get token details", description = "Retrieve details of a specific token")
    @ApiResponse(responseCode = "200", description = "Token details retrieved successfully")
    public TokenResponse getToken(@PathVariable Long tokenId) {
        Token token = tokenService.getToken(tokenId);
        return mapToResponse(token);
    }

    private TokenResponse mapToResponse(Token token) {
        TokenResponse response = new TokenResponse();
        response.setId(token.getId());
        response.setTokenNumber(token.getTokenNumber());
        response.setStatus(token.getStatus());
        response.setCounterName(token.getServiceCounter().getCounterName());
        response.setIssuedAt(token.getIssuedAt());
        response.setCompletedAt(token.getCompletedAt());
        return response;
    }
}