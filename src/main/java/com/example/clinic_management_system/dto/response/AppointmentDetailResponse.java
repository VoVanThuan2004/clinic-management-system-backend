package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDetailResponse {
    private String appointmentId;
    private String patientName;
    private LocalDate dateOfBirth;
    private Integer gender;
    private String phoneNumber;
    private String doctorName;
    private String employeeName;
    private String roomName;
    private String serviceName;
    private Instant startTime;
    private String reason;
    private String status;
}
