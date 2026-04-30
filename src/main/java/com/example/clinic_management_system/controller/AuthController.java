package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.ChangePasswordRequest;
import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.LoginResponse;
import com.example.clinic_management_system.security.CustomUserDetail;
import com.example.clinic_management_system.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        LoginResponse loginResponse = authService.login(loginRequest, httpServletRequest, httpServletResponse);
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

    @PostMapping("v2/logout")
    public ResponseEntity<ApiResponse<?>> logoutAccount(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logoutAccount(refreshToken, response);

        return ResponseEntity.status(HttpStatus.OK.value()).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Đăng xuất tài khoản thành công")
                .build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userId = customUserDetail.getUserId();

        authService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Thay đổi mật khẩu thành công")
                .build());
    }
}
