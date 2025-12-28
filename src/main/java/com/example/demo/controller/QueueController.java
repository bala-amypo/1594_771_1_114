package com.example.demo.controller;

import com.example.demo.dto.QueuePositionResponse;
import com.example.demo.entity.QueuePosition;
import com.example.demo.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
@Tag(name = "Queue Management", description = "Queue position management")
@SecurityRequirement(name = "bearerAuth")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PutMapping("/position/{tokenId}/{newPosition}")
    @Operation(summary = "Update queue position", description = "Update the queue position for a token")
    @ApiResponse(responseCode = "200", description = "Queue position updated successfully")
    public QueuePositionResponse updateQueuePosition(@PathVariable Long tokenId, @PathVariable Integer newPosition) {
        QueuePosition position = queueService.updateQueuePosition(tokenId, newPosition);
        return mapToResponse(position);
    }

    @GetMapping("/position/{tokenId}")
    @Operation(summary = "Get queue position", description = "Get the current queue position for a token")
    @ApiResponse(responseCode = "200", description = "Queue position retrieved successfully")
    public QueuePositionResponse getPosition(@PathVariable Long tokenId) {
        QueuePosition position = queueService.getPosition(tokenId);
        return mapToResponse(position);
    }

    private QueuePositionResponse mapToResponse(QueuePosition position) {
        QueuePositionResponse response = new QueuePositionResponse();
        response.setTokenId(position.getToken().getId());
        response.setPosition(position.getPosition());
        response.setTokenNumber(position.getToken().getTokenNumber());
        return response;
    }
}