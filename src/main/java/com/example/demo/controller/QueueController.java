package com.example.demo.controller;

import com.example.demo.entity.QueuePosition;
import com.example.demo.service.impl.QueueServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueServiceImpl service;

    public QueueController(QueueServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{tokenId}")
    public QueuePosition updateQueue(
            @PathVariable Long tokenId,
            @RequestParam Integer position) {
        return service.updateQueuePosition(tokenId, position);
    }

    @GetMapping("/{tokenId}")
    public QueuePosition getQueue(@PathVariable Long tokenId) {
        return service.getPosition(tokenId);
    }
}
