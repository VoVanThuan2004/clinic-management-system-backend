package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LogoutRequest {
    private String userId;
    private String refreshToken;
}
