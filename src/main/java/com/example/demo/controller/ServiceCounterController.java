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
    public ServiceCounter create(@RequestBody ServiceCounter counter) {
        return counterService.createCounter(counter);
    }

    @GetMapping
    public List<ServiceCounter> getAll() {
        return counterService.getAllCounters();
    }

    @GetMapping("/active")
    public List<ServiceCounter> getActive() {
        return counterService.getActiveCounters();
    }

    @GetMapping("/{id}")
    public ServiceCounter getById(@PathVariable Long id) {
        return counterService.getCounterById(id);
    }
}
