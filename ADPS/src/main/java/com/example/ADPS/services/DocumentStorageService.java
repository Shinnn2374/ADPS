package com.example.ADPS.services;

import com.example.ADPS.exceptions.DocumentNotFoundException;
import com.example.ADPS.model.Document;
import com.example.ADPS.model.DocumentContent;
import com.example.ADPS.repository.DocumentContentRepository;
import com.example.ADPS.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentStorageService {

    private final DocumentRepository documentRepository;
    private final DocumentContentRepository contentRepository;
    private final String storagePath;


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
}