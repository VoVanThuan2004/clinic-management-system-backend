package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class CategoryResponse {
    private String categoryId;
    private String categoryName;
    private Instant createdAt;
    private Instant updatedAt;
}
