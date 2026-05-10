package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.CategoryRequest;
import com.example.clinic_management_system.dto.response.CategoryOptionResponse;
import com.example.clinic_management_system.dto.response.CategoryResponse;
import com.example.clinic_management_system.entity.Category;
import com.example.clinic_management_system.entity.Medicine;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.CategoryMapper;
import com.example.clinic_management_system.repository.CategoryRepository;
import com.example.clinic_management_system.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MedicineRepository medicineRepository;

    @Override
    public void createCategory(CategoryRequest categoryRequest) {
        if (categoryRequest.getCategoryName() == null) {
            throw new BadRequestException("Vui lòng nhập tên danh mục");
        }

        Category category = categoryMapper.toEntity(categoryRequest);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(String categoryId, CategoryRequest categoryRequest) {
        // 1. Kiểm tra danh mục có hợp lệ
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Danh mục không tồn tại");
        }

        // 2. Kiểm tra data body
        if (categoryRequest.getCategoryName() == null) {
            throw new BadRequestException("Vui lòng nhập tên danh mục");
        }

        category.get().setCategoryName(categoryRequest.getCategoryName());
        categoryRepository.save(category.get());
    }

    @Override
    public void deleteCategory(String categoryId) {
        // 1. Kiểm tra danh mục có hợp lệ
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Danh mục không tồn tại");
        }

        // 2. Kiểm tra xem có thuốc nào đang tồn tại với danh mục này không
        boolean medicineExist = medicineRepository.existsByCategoryId(categoryId);
        if (medicineExist) {
            throw new BadRequestException("Không thể xóa, danh mục hiện tại đang có thuốc tồn tại");
        }

        // 3. Xóa danh mục
        categoryRepository.delete(category.get());
    }

    @Override
    public Page<CategoryResponse> getAllCategories(int page, int size, String search) {
        // 1. Tạo đối tượng phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 2. Query trả về data
        Page<Category> categories = categoryRepository.findAllCategories(search, pageable);

        // 3. Mapping data
        Page<CategoryResponse> categoryResponses = categories.map(categoryMapper::toResponse);

        return categoryResponses;
    }

    @Override
    public List<CategoryOptionResponse> getAllCategoriesOptions() {

        // 1. Query trả về data
        List<Category> categories = categoryRepository.findAll(Sort.by("createdAt").descending());

        // 2. Mapping data trả về
        List<CategoryOptionResponse> categoryOptionResponses = categoryMapper.toOptionResponseList(categories);

        return categoryOptionResponses;
    }
}
