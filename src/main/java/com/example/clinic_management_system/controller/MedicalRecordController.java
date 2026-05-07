package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.MedicalRecordRequest;
import com.example.clinic_management_system.dto.request.MedicalRecordUpdateRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.MedicalRecordPDFResponse;
import com.example.clinic_management_system.dto.response.MedicalRecordResponse;
import com.example.clinic_management_system.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createMedicalRecord (@Valid @RequestBody MedicalRecordRequest medicalRecordRequest) {
        String medicalRecordId = medicalRecordService.createMedicalRecord(medicalRecordRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo hồ sơ bệnh lý thành công")
                        .data(medicalRecordId)
                .build());
    }

    @PutMapping("/{medicalRecordId}")
    public ResponseEntity<ApiResponse<?>> updateMedicalRecord (
            @PathVariable String medicalRecordId,
            @RequestBody MedicalRecordUpdateRequest medicalRecordUpdateRequest
    ) {
        medicalRecordService.updateMedicalRecord(medicalRecordId, medicalRecordUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật hồ sơ bệnh lý thành công")
                .build());
    }

    // Lấy danh sách hồ sơ khám
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<MedicalRecordResponse>>> getMedicalRecords (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) boolean paymentStatus
    ) {
        Page<MedicalRecordResponse> medicalRecordResponses = medicalRecordService.getMedicalRecords(page, size, search, doctorId, paymentStatus);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<MedicalRecordResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách hồ sơ bệnh lý")
                        .data(medicalRecordResponses)
                .build());
    }

    // Lấy chi tiết hồ sơ bệnh lý, tải pdf
    @GetMapping("/{recordId}/pdf")
    public ResponseEntity<ApiResponse<MedicalRecordPDFResponse>> getMedicalRecordDetailPDF(@PathVariable String recordId) {

        MedicalRecordPDFResponse medicalRecordResponse = medicalRecordService.getMedicalRecordDetailPDF(recordId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<MedicalRecordPDFResponse>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy chi tiết hồ sơ bệnh lý thành công")
                        .data(medicalRecordResponse)
                .build());
    }

    // Lấy chi tiết hồ sơ bệnh lý
    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> getMedicalRecordDetail(@PathVariable String recordId) {
        MedicalRecordResponse medicalRecordResponse = medicalRecordService.getMedicalRecordDetail(recordId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<MedicalRecordResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết hồ sơ bệnh lý thành công")
                .data(medicalRecordResponse)
                .build());
    }

    // Kiểm tra hồ sơ khám có tồn tại theo lịch hẹn khám
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<?>> checkMedicalRecord(@RequestParam String appointmentId) {
        String medicalRecordId = medicalRecordService.checkMedicalRecord(appointmentId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Kiểm tra hồ sơ bệnh lý thành công")
                        .data(medicalRecordId)
                .build());
    }
}
