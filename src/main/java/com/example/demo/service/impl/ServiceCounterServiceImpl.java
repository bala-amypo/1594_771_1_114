package com.example.demo.service.impl;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.repository.ServiceCounterRepository;
import java.util.List;

public class ServiceCounterServiceImpl {

    private final ServiceCounterRepository repo;

    public ServiceCounterServiceImpl(ServiceCounterRepository repo) {
        this.repo = repo;
    }

    public ServiceCounter addCounter(ServiceCounter counter) {
        return repo.save(counter);
    }

    public List<ServiceCounter> getActiveCounters() {
        return repo.findByIsActiveTrue();
    }
}
