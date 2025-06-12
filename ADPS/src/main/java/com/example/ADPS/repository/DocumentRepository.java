package com.example.ADPS.repository;

import com.example.ADPS.model.Document;
import com.example.ADPS.model.ProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    int countByStatus(ProcessingStatus processingStatus);
}
