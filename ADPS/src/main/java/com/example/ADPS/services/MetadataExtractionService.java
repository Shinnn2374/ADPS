package com.example.ADPS.services;

import com.example.ADPS.exceptions.DocumentProcessingException;
import com.example.ADPS.model.Document;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetadataExtractionService {

    public Map<String, String> extractMetadata(Document document) {
        try {
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            parser.parse(
                    new ByteArrayInputStream(document.getDocumentContent().getContent()),
                    handler,
                    metadata,
                    context
            );

            Map<String, String> result = new HashMap<>();
            for (String name : metadata.names()) {
                result.put(name, metadata.get(name));
            }

            // Добавляем базовые метаданные
            result.put("originalName", document.getTitleName());
            result.put("fileType", document.getFileType());
            result.put("fileSize", String.valueOf(document.getFullSize()));

            return result;
        } catch (Exception e) {
            throw new DocumentProcessingException("Failed to extract metadata");
        }
    }
}