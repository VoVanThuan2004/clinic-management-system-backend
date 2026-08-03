package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.KnowledgeDocumentRequest;
import com.example.clinic_management_system.dto.response.KnowledgeDocumentResponse;
import com.example.clinic_management_system.entity.KnowledgeDocument;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeDocumentMapper {

    public KnowledgeDocumentResponse mapToResponse(KnowledgeDocument knowledgeDocument) {
        return KnowledgeDocumentResponse.builder()
                .documentId(knowledgeDocument.getDocumentId())
                .title(knowledgeDocument.getTitle())
                .category(knowledgeDocument.getCategory())
                .content(knowledgeDocument.getContent())
                .tags(knowledgeDocument.getTags())
                .version(knowledgeDocument.getVersion())
                .status(knowledgeDocument.getStatus())
                .createdAt(knowledgeDocument.getCreatedAt())
                .updatedAt(knowledgeDocument.getUpdatedAt())
                .build();
    }

    public KnowledgeDocument mapToEntity(KnowledgeDocumentRequest knowledgeDocumentRequest) {
        return KnowledgeDocument.builder()
                .title(knowledgeDocumentRequest.getTitle())
                .category(knowledgeDocumentRequest.getCategory())
                .content(knowledgeDocumentRequest.getContent())
                .tags(knowledgeDocumentRequest.getTags())
                .build();
    }
}
