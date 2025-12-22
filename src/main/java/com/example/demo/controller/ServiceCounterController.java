package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/counters")
@Tag(name = "Service Counters")
public class ServiceCounterController {

    @PostMapping
    @Operation(summary = "Add service counter")
    public String addCounter() {
        return "Counter added";
    }

    @GetMapping("/active")
    @Operation(summary = "Get active counters")
    public String getActiveCounters() {
        return "Active counters";
    }
}
