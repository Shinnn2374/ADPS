package com.example.ADPS.dto;

import com.example.ADPS.model.ProcessingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class ProcessingStatusDto {
    private Long documentId;
    private ProcessingStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private Long processingTimeSeconds;
    private Integer progressPercentage;
    private String currentOperation;

    public ProcessingStatusDto(Long documentId, ProcessingStatus status,
                               LocalDateTime startedAt, String currentOperation) {
        this.documentId = documentId;
        this.status = status;
        this.startedAt = startedAt;
        this.updatedAt = LocalDateTime.now();
        this.processingTimeSeconds = startedAt != null
                ? ChronoUnit.SECONDS.between(startedAt, this.updatedAt)
                : null;
        this.currentOperation = currentOperation;
    }
}