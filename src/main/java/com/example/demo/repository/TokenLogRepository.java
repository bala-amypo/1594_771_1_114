package com.example.demo.repository;

import com.example.demo.entity.TokenLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenLogRepository extends JpaRepository<TokenLog, Long> {

    List<TokenLog> findByToken_IdOrderByLoggedAtAsc(Long tokenId);
}
