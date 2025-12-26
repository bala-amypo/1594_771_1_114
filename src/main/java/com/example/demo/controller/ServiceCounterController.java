package com.example.demo.controller;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.impl.ServiceCounterServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counter")
public class ServiceCounterController {

    private final ServiceCounterServiceImpl service;

    public ServiceCounterController(ServiceCounterServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ServiceCounter addCounter(@RequestBody ServiceCounter counter) {
        return service.addCounter(counter);
    }

    @GetMapping
    public List<ServiceCounter> getActiveCounters() {
        return service.getActiveCounters();
    }
}
