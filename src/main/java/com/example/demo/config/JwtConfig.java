package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(
                "my-secret-key-my-secret-key-my-secret-key", // min 32 chars
                3600000 // 1 hour
        );
    }
}
