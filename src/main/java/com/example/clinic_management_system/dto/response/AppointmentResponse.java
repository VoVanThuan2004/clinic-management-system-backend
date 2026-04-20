package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class AppointmentResponse {
    private String appointmentId;
    private String patientName;
    private String phoneNumber;
    private String doctorName;
    private String avatarUrl;
    private String roomName;
    private String serviceName;
    private Instant startTime;
    private String reason;
    private String status;
}
