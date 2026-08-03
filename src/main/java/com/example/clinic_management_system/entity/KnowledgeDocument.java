package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "knowledge_document")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnowledgeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String documentId;

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private String status = "draft";

    @Version
    @Builder.Default
    private Integer version = 1;

    private String tags;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
