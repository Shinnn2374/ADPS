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
        try {
            return ResponseEntity.ok(processingService.getProcessingStatus(id, detailed));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/queue")
    public ResponseEntity<Integer> getProcessingQueueSize() {
        return ResponseEntity.ok(processingService.getQueueSize());
    }

    @PostMapping("/document/{id}/cancel")
    public ResponseEntity<Void> cancelProcessing(@PathVariable Long id) {
        try {
            processingService.cancelProcessing(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}