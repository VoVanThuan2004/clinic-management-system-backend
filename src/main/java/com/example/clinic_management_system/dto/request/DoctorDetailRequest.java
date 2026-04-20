package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailRequest {
    private String specialty;
    private Integer experienceYears;
    private String biography;
}
