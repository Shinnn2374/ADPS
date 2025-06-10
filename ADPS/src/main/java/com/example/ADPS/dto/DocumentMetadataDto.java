package com.example.ADPS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadataDto {
    private Long documentId;
    private String originalFilename;
    private String fileType;
    private Long fileSize;
    private Map<String, String> metadata;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
}