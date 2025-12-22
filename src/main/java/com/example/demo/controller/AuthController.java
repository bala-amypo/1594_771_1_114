package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "User login")
    public String login() {
        return "Login success";
    }

    @PostMapping("/register")
    @Operation(summary = "User registration")
    public String register() {
        return "User registered";
    }
}
