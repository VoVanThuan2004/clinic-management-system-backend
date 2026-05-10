package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.CategoryRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.CategoryOptionResponse;
import com.example.clinic_management_system.dto.response.CategoryResponse;
import com.example.clinic_management_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createCategory (@RequestBody CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo danh mục thành công")
                .build());
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateCategory (@PathVariable String categoryId, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(categoryId, categoryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật danh mục thành công")
                .build());
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteCategory (@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa danh mục thành công")
                .build());
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<CategoryResponse> categoryResponses = categoryService.getAllCategories(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<CategoryResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách danh mục thành công")
                .data(categoryResponses)
                .build());
    }

    // Trả về danh sách dạng select options
    @GetMapping("/options")
    public ResponseEntity<ApiResponse<List<CategoryOptionResponse>>> getAllCategoriesOptions() {
        List<CategoryOptionResponse> categoryResponses = categoryService.getAllCategoriesOptions();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<CategoryOptionResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách lựa chọn danh mục thành công")
                .data(categoryResponses)
                .build());
    }

}
