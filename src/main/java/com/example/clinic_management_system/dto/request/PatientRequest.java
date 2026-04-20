package com.example.clinic_management_system.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String fullName;
    private Integer gender;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
}
