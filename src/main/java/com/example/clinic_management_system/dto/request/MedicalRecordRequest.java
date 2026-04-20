package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordRequest {
    @NotBlank(message = "Lịch hẹn không được để trống")
    private String appointmentId;
}
