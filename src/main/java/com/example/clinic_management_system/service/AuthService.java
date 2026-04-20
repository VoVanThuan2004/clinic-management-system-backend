package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest);

    void logout(LogoutRequest logoutRequest);
}
