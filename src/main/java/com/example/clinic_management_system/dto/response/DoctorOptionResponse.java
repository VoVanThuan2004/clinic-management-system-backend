package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class DoctorOptionResponse {
    private String doctorId;
    private String doctorName;
    private String specialty;
}
