package com.example.ADPS.exceptions;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super(id + " not found");
    }
}
