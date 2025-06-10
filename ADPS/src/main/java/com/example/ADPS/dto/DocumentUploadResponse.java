package com.example.ADPS.dto;

import com.example.ADPS.model.ProcessingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentUploadResponse {
    private Long documentId;
    private String originalFilename;
    private ProcessingStatus status;
    private LocalDateTime uploadTime;
    private String message;

    public DocumentUploadResponse(Long documentId, String originalFilename, ProcessingStatus status) {
        this.documentId = documentId;
        this.originalFilename = originalFilename;
        this.status = status;
        this.uploadTime = LocalDateTime.now();
        this.message = "Document successfully uploaded and queued for processing";
    }
}