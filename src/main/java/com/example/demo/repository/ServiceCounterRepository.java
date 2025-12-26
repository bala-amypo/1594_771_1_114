package com.example.demo.repository;

import com.example.demo.entity.ServiceCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceCounterRepository extends JpaRepository<ServiceCounter, Long> {

    List<ServiceCounter> findByIsActiveTrue();
}
