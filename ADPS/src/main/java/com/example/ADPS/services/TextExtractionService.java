package com.example.ADPS.services;

import com.example.ADPS.model.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class TextExtractionService {

    public String extractText(Document document) throws IOException {
        byte[] content = document.getDocumentContent().getContent();
        String fileType = document.getFileType();

        return switch (fileType) {
            case "application/pdf" -> extractFromPdf(content);
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> extractFromDocx(content);
            default -> throw new UnsupportedOperationException("Unsupported file type: " + fileType);
        };
    }

    private String extractFromPdf(byte[] content) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(content))) {
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractFromDocx(byte[] content) throws IOException {
        try (XWPFDocument docx = new XWPFDocument(new ByteArrayInputStream(content));
             XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {
            return extractor.getText();
        }
    }
}