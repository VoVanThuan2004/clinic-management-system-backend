package com.example.clinic_management_system.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DoctorUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private Integer gender;
    private LocalDate dateOfBirth;
    private DoctorDetailRequest doctorDetail;
}
