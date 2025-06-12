package com.example.ADPS.services;

import com.example.ADPS.exceptions.DocumentNotFoundException;
import com.example.ADPS.model.Document;
import com.example.ADPS.model.ProcessingStatus;
import com.example.ADPS.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class DocumentProcessingService {

    private final DocumentRepository documentRepository;
    private final TextExtractionService textExtractionService;
    private final MetadataExtractionService metadataExtractionService;

    public DocumentProcessingService(DocumentRepository documentRepository,
                                     TextExtractionService textExtractionService,
                                     MetadataExtractionService metadataExtractionService) {
        this.documentRepository = documentRepository;
        this.textExtractionService = textExtractionService;
        this.metadataExtractionService = metadataExtractionService;
    }

    @Async("documentProcessingExecutor")
    @Transactional
    public CompletableFuture<Void> processDocumentAsync(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        try {
            updateDocumentStatus(document, ProcessingStatus.IN_PROGRESS, "Начата обработка");

            // Извлечение текста
            String extractedText = textExtractionService.extractText(document);
            document.setExtractedText(extractedText);

            // Извлечение метаданных
            document.setMetadata(metadataExtractionService.extractMetadata(document));

            updateDocumentStatus(document, ProcessingStatus.COMPLETED, "Обработка завершена");
        } catch (Exception e) {
            log.error("Ошибка обработки документа ID: " + documentId, e);
            updateDocumentStatus(document, ProcessingStatus.FAILED, "Ошибка: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    private void updateDocumentStatus(Document document, ProcessingStatus status, String message) {
        document.setStatus(status);
        document.setStatusMessage(message);
        documentRepository.save(document);
        log.info("Документ ID: {} - {}: {}", document.getId(), status, message);
    }
}