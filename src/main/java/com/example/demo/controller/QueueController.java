package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/queue")
@Tag(name = "Queue")
public class QueueController {

    @PutMapping("/position/{tokenId}/{newPosition}")
    @Operation(summary = "Update queue position")
    public String updatePosition(
            @PathVariable Long tokenId,
            @PathVariable Integer newPosition) {
        return "Queue position updated";
    }

    @GetMapping("/position/{tokenId}")
    @Operation(summary = "Get queue position")
    public String getPosition(@PathVariable Long tokenId) {
        return "Queue position";
    }
}
