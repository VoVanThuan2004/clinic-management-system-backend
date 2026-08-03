package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.KnowledgeDocumentRequest;
import com.example.clinic_management_system.dto.response.KnowledgeDocumentResponse;
import com.example.clinic_management_system.entity.KnowledgeDocument;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.KnowledgeDocumentMapper;
import com.example.clinic_management_system.repository.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KnowledgeDocumentServiceImpl implements KnowledgeDocumentService {
    private final KnowledgeDocumentRepository knowledgeDocumentRepository;
    private final KnowledgeDocumentMapper knowledgeDocumentMapper;

    @Override
    public Page<KnowledgeDocumentResponse> getAllDocuments(int page, int size, String search) {
        // 1. Tạo đối tượng phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 2. Query dữ liệu database
        Page<KnowledgeDocument> knowledgeDocuments = knowledgeDocumentRepository.findAllDocuments(pageable, search);

        // 3. Mapping sang dữ liệu trả về
        return knowledgeDocuments.map(knowledgeDocumentMapper::mapToResponse);
    }

    @Override
    public KnowledgeDocumentResponse createNewDocument(KnowledgeDocumentRequest knowledgeDocumentRequest) {

        KnowledgeDocument knowledgeDocument = knowledgeDocumentRepository
                .save(knowledgeDocumentMapper.mapToEntity(knowledgeDocumentRequest));

        return knowledgeDocumentMapper.mapToResponse(knowledgeDocument);
    }

    @Override
    public KnowledgeDocumentResponse updateDocument(String documentId, KnowledgeDocumentRequest knowledgeDocumentRequest) {
        // 1. Kiểm tra tài liệu có tồn tại
        Optional<KnowledgeDocument> knowledgeDocument = knowledgeDocumentRepository.findById(documentId);
        if (knowledgeDocument.isEmpty()) {
            throw new ResourceNotFoundException("Tài liệu không tồn tại");
        }

        // 2. Cập nhật thông tin tài liệu
        knowledgeDocument.get().setTitle(knowledgeDocumentRequest.getTitle());
        knowledgeDocument.get().setCategory(knowledgeDocumentRequest.getCategory());
        knowledgeDocument.get().setContent(knowledgeDocumentRequest.getContent());
        knowledgeDocument.get().setTags(knowledgeDocumentRequest.getTags());

        // 3. Lưu tài liệu (version tự tăng nhờ @Version)
        KnowledgeDocument updatedKnowledgeDocument = knowledgeDocumentRepository.save(knowledgeDocument.get());

        return knowledgeDocumentMapper.mapToResponse(updatedKnowledgeDocument);
    }

    @Override
    public void deleteDocument(String documentId) {
        // 1. Kiểm tra tài liệu có tồn tại
        Optional<KnowledgeDocument> knowledgeDocument = knowledgeDocumentRepository.findById(documentId);
        if (knowledgeDocument.isEmpty()) {
            throw new ResourceNotFoundException("Tài liệu không tồn tại");
        }

        // 2. Xóa tài liệu
        knowledgeDocumentRepository.delete(knowledgeDocument.get());
    }
}
