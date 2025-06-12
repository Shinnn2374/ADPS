package com.example.ADPS.controller;

import com.example.ADPS.dto.ProcessingStatusDto;
import com.example.ADPS.services.DocumentProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final DocumentProcessingService processingService;

    public StatusController(DocumentProcessingService processingService) {
        this.processingService = processingService;
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<ProcessingStatusDto> getProcessingStatus(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean detailed) {

        return ResponseEntity.ok(processingService.getProcessingStatus(id, detailed));
    }

    @GetMapping("/queue")
    public ResponseEntity<Integer> getProcessingQueueSize() {
        return ResponseEntity.ok(processingService.getQueueSize());
    }

    @PostMapping("/document/{id}/cancel")
    public ResponseEntity<Void> cancelProcessing(@PathVariable Long id) {
        processingService.cancelProcessing(id);
        return ResponseEntity.noContent().build();
    }
}