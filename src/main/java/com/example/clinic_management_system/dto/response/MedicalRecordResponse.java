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
public class MedicalRecordResponse {
    private String medicalRecordId;
    private String doctorName;
    private String patientName;
    private Integer gender;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String symptoms;
    private String diagnosis;
    private String notes;
    private boolean paymentStatus;
}
