package com.example.demo.service;

import com.example.demo.entity.ServiceCounter;
import java.util.List;

public interface ServiceCounterService {

    ServiceCounter createCounter(ServiceCounter counter);

    List<ServiceCounter> getAllCounters();

    List<ServiceCounter> getActiveCounters();

    ServiceCounter getCounterById(Long id);
}
