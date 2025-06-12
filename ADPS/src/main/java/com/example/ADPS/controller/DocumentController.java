package com.example.ADPS.controller;

import com.example.ADPS.dto.DocumentMetadataDto;
import com.example.ADPS.dto.DocumentUploadResponse;
import com.example.ADPS.dto.TextExtractionResultDto;
import com.example.ADPS.model.ProcessingStatus;
import com.example.ADPS.services.DocumentProcessingService;
import com.example.ADPS.services.DocumentStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentStorageService storageService;
    private final DocumentProcessingService processingService;

    public DocumentController(DocumentStorageService storageService,
                              DocumentProcessingService processingService) {
        this.storageService = storageService;
        this.processingService = processingService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new DocumentUploadResponse(null, "File is empty", ProcessingStatus.FAILED));
        }

        try {
            var document = storageService.storeDocument(file);
            processingService.processDocumentAsync(document.getId());

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new DocumentUploadResponse(
                            document.getId(),
                            document.getTitleName(),
                            ProcessingStatus.IN_PROCESSING
                    ));
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DocumentUploadResponse(
                            null,
                            file.getOriginalFilename(),
                            ProcessingStatus.FAILED
                    ));
        }
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<DocumentMetadataDto> getDocumentMetadata(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(storageService.getDocumentMetadata(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/text")
    public ResponseEntity<TextExtractionResultDto> getExtractedText(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(storageService.getExtractedText(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}