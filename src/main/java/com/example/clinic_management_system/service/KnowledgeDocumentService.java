package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.KnowledgeDocumentRequest;
import com.example.clinic_management_system.dto.response.KnowledgeDocumentResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface KnowledgeDocumentService {
    Page<KnowledgeDocumentResponse> getAllDocuments(int page, int size, String search);

    KnowledgeDocumentResponse createNewDocument(@Valid KnowledgeDocumentRequest knowledgeDocumentRequest);

    KnowledgeDocumentResponse updateDocument(String documentId, @Valid KnowledgeDocumentRequest knowledgeDocumentRequest);

    void deleteDocument(String documentId);
}
