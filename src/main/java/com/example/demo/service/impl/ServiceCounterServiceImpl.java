package com.example.demo.service.impl;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.repository.ServiceCounterRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceCounterServiceImpl {
    private final ServiceCounterRepository counterRepository;

    public ServiceCounterServiceImpl(ServiceCounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    public ServiceCounter addCounter(ServiceCounter counter) {
        return counterRepository.save(counter);
    }

    public List<ServiceCounter> getActiveCounters() {
        return counterRepository.findByIsActiveTrue();
    }
}