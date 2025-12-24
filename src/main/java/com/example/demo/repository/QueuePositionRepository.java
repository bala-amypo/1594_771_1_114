package com.example.demo.repository;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueuePositionRepository extends JpaRepository<QueuePosition, Long> {

    Optional<QueuePosition> findByToken_Id(Long tokenId);

    // ✅ REQUIRED to calculate queue position
    long countByToken_ServiceCounter_IdAndToken_Status(
            Long counterId,
            TokenStatus status
    );
}
