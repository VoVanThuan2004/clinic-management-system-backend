package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.RefreshTokenRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RefreshTokenResponse;
import com.example.clinic_management_system.security.CustomUserDetail;
import com.example.clinic_management_system.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refresh-token")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshTokenUser (@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.refreshTokenUser(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<RefreshTokenResponse>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Refresh token thành công")
                        .data(refreshTokenResponse)
                .build());
    }
}
