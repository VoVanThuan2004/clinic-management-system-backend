package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RefreshTokenResponse;

public interface RefreshTokenService {
    void createRefreshTokenForUser(String userId, String userAgent, String ipAddress);

    void deleteRefreshTokenForUser(String userId, String refreshToken);

    RefreshTokenResponse refreshTokenUser(String refreshToken);
}
