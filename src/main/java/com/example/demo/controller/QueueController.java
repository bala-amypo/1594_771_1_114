package com.example.demo.controller;

import com.example.demo.entity.QueuePosition;
import com.example.demo.service.QueueService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PutMapping("/{tokenId}")
    public QueuePosition updateQueuePosition(
            @PathVariable Long tokenId,
            @RequestParam Integer position
    ) {
        return queueService.updateQueuePosition(tokenId, position);
    }

    @GetMapping("/{tokenId}")
    public QueuePosition getPosition(@PathVariable Long tokenId) {
        return queueService.getPosition(tokenId);
    }
}
