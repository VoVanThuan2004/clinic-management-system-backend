package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private String patientId;
    private String doctorId;
    private String employeeId;
    private String roomId;
    private String serviceId;
    private String reason;
    private Instant startTime;
    private Integer durationMinutes;
}
