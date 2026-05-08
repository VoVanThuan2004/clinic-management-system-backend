package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.MedicalServiceRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.MedicalServiceOptionResponse;
import com.example.clinic_management_system.dto.response.MedicalServiceResponse;
import com.example.clinic_management_system.service.MedicalSVService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MedicalServiceController {
    private final MedicalSVService medicalSVService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addService (@RequestBody MedicalServiceRequest medicalServiceRequest) {
        medicalSVService.addService(medicalServiceRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Thêm dịch vụ khám thành công")
                .build());
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateService (@PathVariable String serviceId,
                                                         @RequestBody MedicalServiceRequest medicalServiceRequest
    ) {
        medicalSVService.updateService(serviceId, medicalServiceRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật dịch vụ thành công")
                .build());
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteService (@PathVariable String serviceId) {
        medicalSVService.deleteService(serviceId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa dịch vụ thành công")
                .build());
    }

    // Lấy danh sách phòng khám options
    @GetMapping("options")
    public ResponseEntity<ApiResponse<List<MedicalServiceOptionResponse>>> getServiceOptions() {
        List<MedicalServiceOptionResponse> medicalServiceOptionResponses = medicalSVService.getServiceOptions();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<MedicalServiceOptionResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách lựa chọn dịch vụ thành công")
                .data(medicalServiceOptionResponses)
                .build());
    }

    // Lấy danh sách phòng khám cho admin
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<MedicalServiceResponse>>> getServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<MedicalServiceResponse> medicalServiceResponses = medicalSVService.findAllServices(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<MedicalServiceResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách dịch vụ thành công")
                .data(medicalServiceResponses)
                .build());
    }

}
