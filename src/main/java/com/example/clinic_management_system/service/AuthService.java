package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.ChangePasswordRequest;
import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void logout(LogoutRequest logoutRequest);

    void logoutAccount(String refreshToken, HttpServletResponse response);

    void changePassword(String userId, ChangePasswordRequest changePasswordRequest);
}
