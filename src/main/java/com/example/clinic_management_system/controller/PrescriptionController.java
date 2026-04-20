package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.PrescriptionRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import com.example.clinic_management_system.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    // Tạo toa thuốc
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createPrescription(@Valid @RequestBody PrescriptionRequest prescriptionRequest) {
        prescriptionService.createPrescription(prescriptionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo toa thuốc thành công")
                .build());
    }

    // Lấy chi tiết toa thuốc
    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescription(@PathVariable String recordId) {
        PrescriptionResponse prescriptionResponses = prescriptionService.getPrescription(recordId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<PrescriptionResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết toa thuốc thành công")
                .data(prescriptionResponses)
                .build());
    }
}
