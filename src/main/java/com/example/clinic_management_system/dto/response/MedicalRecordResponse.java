package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordResponse {
    private String medicalRecordId;
    private String doctorName;
    private String patientName;
    private String symptoms;
    private String diagnosis;
    private String notes;
    private boolean paymentStatus;
}
