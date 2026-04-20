package com.example.clinic_management_system.security;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenPayload {
    private String userId;
    private String fullName;
    private String role;
}
