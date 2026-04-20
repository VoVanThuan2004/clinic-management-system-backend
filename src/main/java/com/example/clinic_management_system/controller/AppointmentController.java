package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.AppointmentRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.AppointmentDetailResponse;
import com.example.clinic_management_system.dto.response.AppointmentResponse;
import com.example.clinic_management_system.dto.response.PatientResponse;
import com.example.clinic_management_system.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    // Tạo lịch hẹn
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createAppointment (@RequestBody AppointmentRequest appointmentRequest) {
        appointmentService.createAppointment(appointmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo lịch hẹn thành công")
                .build());
    }

    // Cập nhật lịch hẹn
    @PutMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<?>> updateAppointment (@PathVariable String appointmentId, @RequestBody AppointmentRequest appointmentRequest) {
        appointmentService.updateAppointment(appointmentId, appointmentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật lịch hẹn thành công")
                .build());
    }

    // Cập nhật trạng thái
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<ApiResponse<?>> changeAppointmentStatus (
            @PathVariable String appointmentId,
            @RequestParam String status) {
        appointmentService.changeAppointmentStatus(appointmentId, status);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật trạng thái lịch hẹn thành công")
                .build());
    }

    // Lấy danh sách lịch hẹn khám
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getAllAppointments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        Page<AppointmentResponse> appointments = appointmentService.getAllAppointments(search, date, doctorId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<AppointmentResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách lịch hẹn")
                        .data(appointments)
                .build());
    }

    // Lấy chi tiết 1 lịch hẹn khám
    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentDetailResponse>> getAppointmentDetail(@PathVariable String appointmentId) {
        AppointmentDetailResponse appointmentDetailResponse = appointmentService.getAppointmentDetail(appointmentId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<AppointmentDetailResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết lịch hẹn")
                .data(appointmentDetailResponse)
                .build());
    }

}
