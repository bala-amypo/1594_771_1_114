package com.example.demo.controller;

import com.example.demo.entity.QueuePosition;
import com.example.demo.service.impl.QueueServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
@Tag(name = "Queue Management", description = "Endpoints for queue positions")
public class QueueController {
    private final QueueServiceImpl queueService;

    public QueueController(QueueServiceImpl queueService) {
        this.queueService = queueService;
    }

    @PutMapping("/position/{tokenId}/{newPosition}")
    @Operation(summary = "Update token position in queue")
    public QueuePosition update(@PathVariable Long tokenId, @PathVariable Integer newPosition) {
        return queueService.updateQueuePosition(tokenId, newPosition);
    }

    @getMapping("/position/{tokenId}")
    @Operation(summary = "Get position for a token")
    public QueuePosition get(@PathVariable Long tokenId) {
        return queueService.getPosition(tokenId);
    }
}