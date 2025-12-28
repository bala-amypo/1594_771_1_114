package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.config.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        User created = userService.register(user);
        
        String token = jwtTokenProvider.generateToken(created.getId(), created.getEmail(), "USER");
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(created.getEmail());
        response.setRole("USER");
        return response;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user != null && ("encoded_" + request.getPassword()).equals(user.getPassword())) {
            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), "USER");
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setEmail(user.getEmail());
            response.setRole("USER");
            return response;
        }
        throw new RuntimeException("Invalid credentials");
    }
}