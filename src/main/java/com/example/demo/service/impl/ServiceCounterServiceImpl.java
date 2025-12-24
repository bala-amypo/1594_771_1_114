package com.example.demo.service.impl;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.service.ServiceCounterService;

import java.util.List;

public class ServiceCounterServiceImpl implements ServiceCounterService {

    private final ServiceCounterRepository counterRepository;

    public ServiceCounterServiceImpl(ServiceCounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public ServiceCounter addCounter(ServiceCounter counter) {

        if (counter == null) {
            throw new IllegalArgumentException("Counter cannot be null");
        }

        // default active = true
        if (counter.getIsActive() == null) {
            counter.setIsActive(true);
        }

        // ✅ MUST return saved instance
        ServiceCounter saved = counterRepository.save(counter);
        return saved;
    }

    @Override
    public List<ServiceCounter> getActiveCounters() {
        return counterRepository.findByIsActiveTrue();
    }
}
