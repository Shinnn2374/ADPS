package com.example.ADPS.repository;

import com.example.ADPS.model.DocumentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentContentRepository extends JpaRepository<DocumentContent, Long> {
}
