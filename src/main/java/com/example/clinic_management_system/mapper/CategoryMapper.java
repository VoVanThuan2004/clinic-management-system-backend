package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.CategoryRequest;
import com.example.clinic_management_system.dto.response.CategoryOptionResponse;
import com.example.clinic_management_system.dto.response.CategoryResponse;
import com.example.clinic_management_system.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        return category;
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public List<CategoryOptionResponse> toOptionResponseList(List<Category> categories) {
        return categories.stream()
                .map(category -> {
                    return CategoryOptionResponse.builder()
                            .categoryId(category.getCategoryId())
                            .categoryName(category.getCategoryName())
                            .build();
                })
                .toList();
    }
}
