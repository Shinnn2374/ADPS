package com.example.ADPS.services;

import com.example.ADPS.dto.DocumentMetadataDto;
import com.example.ADPS.dto.TextExtractionResultDto;
import com.example.ADPS.exceptions.DocumentNotFoundException;
import com.example.ADPS.exceptions.DocumentProcessingException;
import com.example.ADPS.model.Document;
import com.example.ADPS.model.DocumentContent;
import com.example.ADPS.repository.DocumentContentRepository;
import com.example.ADPS.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class DocumentStorageService {

    private final DocumentRepository documentRepository;
    private final DocumentContentRepository contentRepository;
    private final String storagePath;

    public DocumentStorageService(DocumentRepository documentRepository,
                                  DocumentContentRepository contentRepository,
                                  @Value("${document.storage.path}") String storagePath) {
        this.documentRepository = documentRepository;
        this.contentRepository = contentRepository;
        this.storagePath = storagePath;
    }

    @Transactional
    public Document storeDocument(MultipartFile file) throws IOException {
        Document document = new Document();
        document.setTitleName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFullSize(file.getSize());
        document = documentRepository.save(document);

        DocumentContent content = new DocumentContent();
        content.setContent(file.getBytes());
        content.setDocument(document);
        contentRepository.save(content);

        return document;
    }

    public DocumentContent getDocumentContent(Long documentId) {
        return contentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
    }

    public String generateStoragePath() {
        return storagePath + "/" + UUID.randomUUID();
    }

    public DocumentMetadataDto getDocumentMetadata(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        return new DocumentMetadataDto(
                document.getId(),
                document.getTitleName(),
                document.getFileType(),
                document.getFullSize(),
                document.getMetadata(),
                document.getStatus().name(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public TextExtractionResultDto getExtractedText(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        if (document.getExtractedText() == null) {
            throw new DocumentProcessingException("Text not extracted yet");
        }

        return new TextExtractionResultDto(
                document.getId(),
                document.getTitleName(),
                document.getExtractedText(),
                document.getExtractedText().length()
        );
    }
}