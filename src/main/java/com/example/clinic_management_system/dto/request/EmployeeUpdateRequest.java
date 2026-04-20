package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private Integer gender;
    private LocalDate dateOfBirth;
}
