package com.example.ADPS.dto;

import com.example.ADPS.model.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingStatusDto {
    private Long documentId;
    private ProcessingStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private Long processingTimeSeconds;
    private Integer progressPercentage;
    private String currentOperation;
}