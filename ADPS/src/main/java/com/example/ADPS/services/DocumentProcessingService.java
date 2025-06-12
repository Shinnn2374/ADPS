package com.example.ADPS.services;

import com.example.ADPS.dto.ProcessingStatusDto;
import com.example.ADPS.exceptions.DocumentNotFoundException;
import com.example.ADPS.model.Document;
import com.example.ADPS.model.ProcessingStatus;
import com.example.ADPS.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DocumentProcessingService {

    private final DocumentRepository documentRepository;
    private final TextExtractionService textExtractionService;
    private final MetadataExtractionService metadataExtractionService;
    private final ConcurrentHashMap<Long, Boolean> cancellationMap = new ConcurrentHashMap<>();

    public DocumentProcessingService(DocumentRepository documentRepository,
                                     TextExtractionService textExtractionService,
                                     MetadataExtractionService metadataExtractionService) {
        this.documentRepository = documentRepository;
        this.textExtractionService = textExtractionService;
        this.metadataExtractionService = metadataExtractionService;
    }

    private void updateDocumentStatus(Document document, ProcessingStatus status, String message) {
        document.setStatus(status);
        documentRepository.save(document);
        log.info("Документ ID: {} - {}: {}", document.getId(), status, message);
    }



    public ProcessingStatusDto getProcessingStatus(Long id, boolean detailed) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        return new ProcessingStatusDto(
                document.getId(),
                document.getStatus(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                100L,
                100,
                detailed ? document.getExtractedText() : null
        );
    }

    public int getQueueSize() {
        return documentRepository.countByStatus(ProcessingStatus.IN_PROCESSING);
    }

    public void cancelProcessing(Long id) {
        cancellationMap.put(id, true);
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        document.setStatus(ProcessingStatus.FAILED);
        documentRepository.save(document);
    }

    @Async("documentProcessingExecutor")
    @Transactional
    public CompletableFuture<Void> processDocumentAsync(Long documentId) {
        if (cancellationMap.getOrDefault(documentId, false)) {
            log.info("Processing cancelled for document ID: {}", documentId);
            return CompletableFuture.completedFuture(null);
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
        try {
            updateDocumentStatus(document, ProcessingStatus.IN_PROCESSING, "Processing started");

            if (cancellationMap.getOrDefault(documentId, false)) {
                throw new InterruptedException("Processing cancelled");
            }

            String extractedText = textExtractionService.extractText(document);
            document.setExtractedText(extractedText);

            if (cancellationMap.getOrDefault(documentId, false)) {
                throw new InterruptedException("Processing cancelled");
            }

            document.setMetadata(metadataExtractionService.extractMetadata(document));

            updateDocumentStatus(document, ProcessingStatus.PROCESSED, "Processing completed");
        } catch (InterruptedException e) {
            log.info("Processing interrupted for document ID: {}", documentId);
            updateDocumentStatus(document, ProcessingStatus.FAILED, "Processing cancelled");
        } catch (Exception e) {
            log.error("Error processing document ID: " + documentId, e);
            updateDocumentStatus(document, ProcessingStatus.FAILED, "Error: " + e.getMessage());
        } finally {
            cancellationMap.remove(documentId);
        }
        return CompletableFuture.completedFuture(null);
    }
}

