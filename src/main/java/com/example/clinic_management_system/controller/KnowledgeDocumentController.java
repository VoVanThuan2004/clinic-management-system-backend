package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.KnowledgeDocumentRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.KnowledgeDocumentResponse;
import com.example.clinic_management_system.service.KnowledgeDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledge-documents")
@PreAuthorize("hasRole('ADMIN')")
public class KnowledgeDocumentController {
    private final KnowledgeDocumentService knowledgeDocumentService;

    // Lấy danh sách tài liệu
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<KnowledgeDocumentResponse>>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<KnowledgeDocumentResponse> knowledgeDocumentResponses = knowledgeDocumentService.getAllDocuments(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<KnowledgeDocumentResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách tài liệu thành công")
                        .data(knowledgeDocumentResponses)
                .build());
    }

    // Tạo tài liệu mới
    @PostMapping("")
    public ResponseEntity<ApiResponse<KnowledgeDocumentResponse>> createNewDocument(
            @Valid @RequestBody KnowledgeDocumentRequest knowledgeDocumentRequest)
    {
        KnowledgeDocumentResponse knowledgeDocumentResponse = knowledgeDocumentService.createNewDocument(knowledgeDocumentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<KnowledgeDocumentResponse>builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo mới tài liệu thành công")
                        .data(knowledgeDocumentResponse)
                .build());
    }

    // Cập nhật tài liệu
    @PutMapping("/{documentId}")
    public ResponseEntity<ApiResponse<KnowledgeDocumentResponse>> updateDocument(
            @PathVariable String documentId,
            @Valid @RequestBody KnowledgeDocumentRequest knowledgeDocumentRequest)
    {
        KnowledgeDocumentResponse knowledgeDocumentResponse = knowledgeDocumentService.updateDocument(documentId, knowledgeDocumentRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<KnowledgeDocumentResponse>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Cập nhật tài liệu thành công")
                        .data(knowledgeDocumentResponse)
                .build());
    }

    // Xóa tài liệu
    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse<?>> deleteDocument(@PathVariable String documentId) {
        knowledgeDocumentService.deleteDocument(documentId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Xóa tài liệu thành công")
                .build());
    }
}
