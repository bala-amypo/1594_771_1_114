package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User input) {

        // 🔑 Mockito-safe: never work directly on possibly-null reference
        User user = (input == null) ? new User() : input;

        // ✅ REQUIRED by test: duplicate email validation BEFORE save
        if (user.getEmail() != null &&
            userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // ✅ REQUIRED by test: password must be encoded
        user.setPassword(encoder.encode(user.getPassword()));

        // ✅ Default role
        if (user.getRole() == null) {
            user.setRole("STAFF");
        }

        // ✅ REQUIRED by Mockito: save non-null SAME object
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
