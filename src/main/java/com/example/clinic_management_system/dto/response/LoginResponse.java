package com.example.clinic_management_system.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class LoginResponse {
    private String userId;
    private String fullName;
    private String avatarUrl;
    private String role;
    private String accessToken;
}
