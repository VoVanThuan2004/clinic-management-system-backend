package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class KnowledgeDocumentResponse {
    private String documentId;
    private String title;
    private String category;
    private String content;
    private String status;
    private Integer version;
    private String tags;
    private Instant createdAt;
    private Instant updatedAt;
}
