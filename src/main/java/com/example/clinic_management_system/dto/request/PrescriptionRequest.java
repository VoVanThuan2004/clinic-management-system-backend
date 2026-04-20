package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionRequest {
    @NotBlank(message = "Hồ sơ bệnh lý không được trống")
    private String medicalRecordId;
}
