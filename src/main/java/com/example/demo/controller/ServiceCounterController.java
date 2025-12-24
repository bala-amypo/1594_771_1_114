package com.example.demo.controller;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.ServiceCounterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counters")
public class ServiceCounterController {

    private final ServiceCounterService serviceCounterService;

    public ServiceCounterController(ServiceCounterService serviceCounterService) {
        this.serviceCounterService = serviceCounterService;
    }

    @PostMapping
    public ServiceCounter addCounter(@RequestBody ServiceCounter counter) {
        return serviceCounterService.addCounter(counter);
    }

    @GetMapping("/active")
    public List<ServiceCounter> getActiveCounters() {
        return serviceCounterService.getActiveCounters();
    }
}
