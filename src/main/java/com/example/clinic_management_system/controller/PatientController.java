package com.example.clinic_management_system.controller;


import com.example.clinic_management_system.dto.request.PatientRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.PatientResponse;
import com.example.clinic_management_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {
    private final PatientService patientService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> addPatient (@RequestBody PatientRequest patientRequest) {
        patientService.addPatient(patientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Thêm bệnh nhân thành công")
                .build());
    }

    // Thêm danh sách bệnh nhân
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<?>> addListPatient (@RequestBody List<PatientRequest> patientRequestList) {
        patientService.addListPatient(patientRequestList);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Thêm danh sách bệnh nhân thành công")
                .build());
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<ApiResponse<?>> updatePatient (@PathVariable String patientId, @RequestBody PatientRequest patientRequest) {
        patientService.updatePatient(patientId, patientRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật bệnh nhân thành công")
                .build());
    }

    // Xóa 1 bệnh nhân
    @DeleteMapping("/{patientId}")
    public ResponseEntity<ApiResponse<?>> deletePatient (@PathVariable String patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa bệnh nhân thành công")
                .build());
    }

    // Xóa 1 hoặc nhiều bệnh nhân (bulk delete)
    @DeleteMapping("")
    public ResponseEntity<ApiResponse<?>> deletePatients (@RequestBody List<String> ids) {
        patientService.deletePatients(ids);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa danh sách bệnh nhân thành công")
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<PatientResponse>>> getAllPatients (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<PatientResponse> patientResponses = patientService.getAllPatientsPagination(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<PatientResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách bệnh nhân")
                .data(patientResponses)
                .build());
    }

    // Lấy danh sách bệnh nhân export theo search
    @GetMapping("/export")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatientsExport(
            @RequestParam(defaultValue = "") String search
    ) {
        List<PatientResponse> patientResponses = patientService.getAllPatientsExport(search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<PatientResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Export danh sách bệnh nhân thành công")
                .data(patientResponses)
                .build());
    }

}
