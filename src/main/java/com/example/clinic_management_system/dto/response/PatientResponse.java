package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {
    private String patientId;
    private String patientCode;
    private String fullName;
    private Integer gender;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
}
