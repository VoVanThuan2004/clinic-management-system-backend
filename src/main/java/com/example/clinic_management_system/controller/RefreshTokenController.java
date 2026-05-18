package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RefreshTokenResponse;
import com.example.clinic_management_system.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/refresh-token")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshTokenUser (
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.refreshTokenUser(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<RefreshTokenResponse>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Refresh token thành công")
                        .data(refreshTokenResponse)
                .build());
    }
}
