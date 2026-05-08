package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class DoctorResponse {
    private String doctorId;
    private String doctorName;
    private String email;
    private String avatarUrl;
    private int gender;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private DoctorDetailResponse doctorDetailResponse;
}
