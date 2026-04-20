package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.MedicineRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.MedicineResponse;
import com.example.clinic_management_system.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medicines")
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addMedicine (
            @Valid @RequestPart("data") MedicineRequest medicineRequest,
            @RequestPart(value = "file") MultipartFile file
    ) {
        medicineService.addMedicine(medicineRequest, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Thêm thuốc thành công")
                .build());
    }

    // Cập nhật thông tin thuốc
    @PutMapping("/{medicineId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateMedicine (
            @PathVariable String medicineId,
            @Valid @RequestPart("data") MedicineRequest medicineRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        medicineService.updateMedicine(medicineId, medicineRequest, file);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật thuốc thành công")
                .build());
    }

    // Xóa thuốc
    @DeleteMapping("/{medicineId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteMedicine (@PathVariable String medicineId) {
        medicineService.deleteMedicine(medicineId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa thuốc thành công")
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<MedicineResponse>>> getMedicinesByCategory(
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<MedicineResponse> medicineResponses = medicineService.getMedicinesByCategory(categoryId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<MedicineResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách thuốc thành công")
                .data(medicineResponses)
                .build());
    }
}
