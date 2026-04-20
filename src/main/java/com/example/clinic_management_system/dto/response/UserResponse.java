package com.example.clinic_management_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String fullName;
    private String email;
    private String avatarUrl;
    private int gender;
    private String phoneNumber;
    private String role;
}