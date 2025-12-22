package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tokens")
@Tag(name = "Tokens")
public class TokenController {

    @PostMapping("/issue/{counterId}")
    @Operation(summary = "Issue token")
    public String issue(@PathVariable Long counterId) {
        return "Token issued";
    }
}
