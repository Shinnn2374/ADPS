package com.example.ADPS.dto;

import lombok.Data;

import java.util.List;

@Data
public class TextExtractionResultDto {
    private Long documentId;
    private String originalFilename;
    private String fullText;
    private String textPreview;
    private Integer pageCount;
    private List<String> paragraphs;
    private Integer wordCount;
    private Integer characterCount;

    public TextExtractionResultDto(Long documentId, String originalFilename,
                                   String fullText, Integer pageCount) {
        this.documentId = documentId;
        this.originalFilename = originalFilename;
        this.fullText = fullText;
        this.textPreview = fullText != null && fullText.length() > 200
                ? fullText.substring(0, 200) + "..."
                : fullText;
        this.pageCount = pageCount;
        this.paragraphs = fullText != null
                ? List.of(fullText.split("\n\n"))
                : List.of();
        this.wordCount = fullText != null
                ? fullText.split("\\s+").length
                : 0;
        this.characterCount = fullText != null
                ? fullText.length()
                : 0;
    }
}