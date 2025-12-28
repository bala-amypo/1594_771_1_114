package com.example.demo.repository;

import com.example.demo.entity.QueuePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QueuePositionRepository extends JpaRepository<QueuePosition, Long> {
    Optional<QueuePosition> findByToken_Id(Long tokenId);
}
