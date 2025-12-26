package com.example.demo.repository;

import com.example.demo.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    // Exact signature required for fetching waiting tokens in order
    List<Token> findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(Long counterId, String status);
    
    // Exact signature required for unique token number lookup
    Optional<Token> findByTokenNumber(String tokenNumber);
}