package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.LoginResponse;
import com.example.clinic_management_system.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        LoginResponse loginResponse = authService.login(loginRequest, httpServletRequest);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(loginResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Đã đăng xuất thành công")
                .build();

        return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);
    }
}
