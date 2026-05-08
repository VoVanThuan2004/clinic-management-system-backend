package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.DoctorUpdateRequest;
import com.example.clinic_management_system.dto.request.UserRequest;
import com.example.clinic_management_system.dto.request.EmployeeUpdateRequest;
import com.example.clinic_management_system.dto.request.UserUpdateRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.DoctorOptionResponse;
import com.example.clinic_management_system.dto.response.DoctorResponse;
import com.example.clinic_management_system.dto.response.UserResponse;
import com.example.clinic_management_system.security.CustomUserDetail;
import com.example.clinic_management_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    // Tạo nhân viên mới
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Tạo người dùng thành công")
                .build());
    }



    // Lấy info chi tiết cho profile
    @GetMapping("")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        CustomUserDetail customUserDetail =
                (CustomUserDetail) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String userId = customUserDetail.getUserId();

        UserResponse userResponse = userService.getProfile(userId);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin người dùng thành công")
                .data(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);
    }

    // Lấy danh sách nhân viên
    @GetMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<UserResponse> userResponses = userService.getAllEmployees(page, size, search);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.<Page<UserResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách nhân viên")
                        .data(userResponses)
                        .build()
        );
    }

    // Lấy danh sách bác sĩ
    @GetMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<DoctorResponse>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<DoctorResponse> doctorResponses = userService.getAllDoctors(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<DoctorResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách bác sĩ")
                        .data(doctorResponses)
                .build());
    }

    // Cập nhật thông tin profile
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable String id,
            @Valid @RequestPart("data") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        userService.updateUser(id, userUpdateRequest, file);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Cập nhật thông tin thành công")
                .build());
    }

    // Cập nhật thông tin nhân viên
    @PutMapping("/employees/{id}")
    public ResponseEntity<ApiResponse<?>> updateEmployee(
            @PathVariable String id,
            @RequestPart("data") EmployeeUpdateRequest employeeUpdateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {

        userService.updateEmployee(id, employeeUpdateRequest, file);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Cập nhật thông tin nhân viên thành công")
                        .build());
    }

    // Cập nhật thông tin bác sĩ
    @PutMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<?>> updateDoctor(
            @PathVariable String id,
            @RequestPart("data") DoctorUpdateRequest doctorUpdateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        userService.updateDoctor(id, doctorUpdateRequest, file);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Cập nhật thông tin bác sĩ thành công")
                        .build());
    }

    // Lấy thông tin chọn bác sĩ
    @GetMapping("/doctors/select")
    public ResponseEntity<ApiResponse<List<DoctorOptionResponse>>> getAllDoctorsOption(@RequestParam(required = false) String search) {
        List<DoctorOptionResponse> doctorOptionResponses = userService.getAllDoctorsOption(search);
        
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<DoctorOptionResponse>>builder()
        .status("success")
        .code(HttpStatus.OK.value())
        .message("Lấy danh sách lựa chọn bác sĩ")
        .data(doctorOptionResponses)
        .build());
    }
    
}
