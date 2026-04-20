package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.PrescriptionItemRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.service.PrescriptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class PrescriptionItemController {
    private final PrescriptionItemService prescriptionItemService;

    // Thêm thuốc vào toa
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> addItem(@Valid @RequestBody PrescriptionItemRequest prescriptionItemRequest) {
        prescriptionItemService.addItem(prescriptionItemRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Thêm thuốc vào toa thành công")
                .build());
    }

    // Thay đổi số lượng
    @PutMapping("/{itemId}/quantity")
    public ResponseEntity<ApiResponse<?>> updateQuantity(
            @RequestParam(required = true) Integer newQuantity,
            @PathVariable String itemId
    ) {
        prescriptionItemService.updateQuantity(newQuantity, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Thay đổi số lượng thuốc vào toa thành công")
                .build());
    }

    // Thay đổi ghi chú liều lượng
    @PutMapping("/{itemId}/dosage")
    public ResponseEntity<ApiResponse<?>> updateDosage(
            @RequestParam(required = true) String dosage,
            @PathVariable String itemId
    ) {
        prescriptionItemService.updateDosage(dosage, itemId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật ghi chú liều lượng thuốc vào toa thành công")
                .build());
    }

    // Xóa thuốc ra khỏi toa
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<?>> deleteItem(@PathVariable String itemId) {
        prescriptionItemService.deleteItem(itemId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa thuốc ra khỏi toa thành công")
                .build());
    }
}
