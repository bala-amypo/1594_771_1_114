package com.example.demo.controller;

import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.impl.ServiceCounterServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/counters")
@Tag(name = "Counter Management", description = "Endpoints for service counters")
public class ServiceCounterController {
    private final ServiceCounterServiceImpl counterService;

    public ServiceCounterController(ServiceCounterServiceImpl counterService) {
        this.counterService = counterService;
    }

    @PostMapping("/")
    @Operation(summary = "Add a new counter")
    public ServiceCounter add(@RequestBody ServiceCounter counter) {
        return counterService.addCounter(counter);
    }

    @getMapping("/active")
    @Operation(summary = "List all active counters")
    public List<ServiceCounter> getActive() {
        return counterService.getActiveCounters();
    }
}