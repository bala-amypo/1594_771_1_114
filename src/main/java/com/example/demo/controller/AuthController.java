package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {
    private final UserServiceImpl userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public String login(@RequestBody User user) {
        // Simple mock for login; returns token in real scenario
        return "login-success-token";
    }
}