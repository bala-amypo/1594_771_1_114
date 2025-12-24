package com.example.demo.service.impl;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.service.ServiceCounterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCounterServiceImpl implements ServiceCounterService {

    private final ServiceCounterRepository serviceCounterRepository;

    public ServiceCounterServiceImpl(ServiceCounterRepository serviceCounterRepository) {
        this.serviceCounterRepository = serviceCounterRepository;
    }

    @Override
    public ServiceCounter addCounter(ServiceCounter counter) {

        // ✅ TEST EXPECTATION
        if (counter.getIsActive() == null) {
            counter.setIsActive(true);
        }

        return serviceCounterRepository.save(counter);
    }

    @Override
    public List<ServiceCounter> getActiveCounters() {
        return serviceCounterRepository.findByIsActiveTrue();
    }
}
