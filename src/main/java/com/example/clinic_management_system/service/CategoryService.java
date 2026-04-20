package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.CategoryRequest;
import com.example.clinic_management_system.dto.response.CategoryOptionResponse;
import com.example.clinic_management_system.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequest categoryRequest);

    void updateCategory(String categoryId, CategoryRequest categoryRequest);

    void deleteCategory(String categoryId);

    Page<CategoryResponse> getAllCategories(int page, int size);

    List<CategoryOptionResponse> getAllCategoriesOptions();
}
