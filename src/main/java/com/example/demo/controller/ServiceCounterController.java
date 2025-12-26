package com.example.demo.controller;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.ServiceCounterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counters")
public class ServiceCounterController {

    private final ServiceCounterService counterService;

    public ServiceCounterController(ServiceCounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping
    public ServiceCounter add(@RequestBody ServiceCounter counter) {
        return counterService.addCounter(counter);
    }

    @GetMapping("/active")
    public List<ServiceCounter> activeCounters() {
        return counterService.getActiveCounters();
    }
}
