package com.example.clinic_management_system.dto.response;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private String roleId;
    private String name;
}
