package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class KnowledgeDocumentRequest {
    @NotBlank(message = "Tiêu đề tài liệu không được để trống")
    @Size(max = 255, message = "Tiêu đề tài liệu không được vượt quá 255 ký tự")
    private String title;

    @Size(max = 100, message = "Danh mục không được vượt quá 100 ký tự")
    private String category;

    @NotBlank(message = "Nội dung tài liệu không được để trống")
    private String content;

    @Size(max = 255, message = "Tags không được vượt quá 255 ký tự")
    private String tags;
}
