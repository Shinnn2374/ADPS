package com.example.ADPS.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "documents")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleName;
    private String fileType;
    private Long fullSize;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private DocumentContent documentContent;

    @ElementCollection
    private Map<String, String> metadata = new HashMap<>();

    private String extractedText;
}
