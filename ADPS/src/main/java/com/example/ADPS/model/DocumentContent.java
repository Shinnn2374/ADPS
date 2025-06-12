package com.example.ADPS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "document_content")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "content", nullable = false, columnDefinition = "BYTEA")
    private byte[] content;

    @Column(name = "content_size", nullable = false)
    private Long size;

    @Column(name = "content_checksum", length = 64)
    private String checksum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document;
}
