package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.PatientRequest;
import com.example.clinic_management_system.dto.response.PatientResponse;
import com.example.clinic_management_system.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .patientCode(patient.getPatientCode())
                .fullName(patient.getFullName())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .build();
    }

    public Patient toEntity(PatientRequest patientRequest) {
        return Patient.builder()
                .fullName(patientRequest.getFullName())
                .gender(patientRequest.getGender())
                .phoneNumber(patientRequest.getPhoneNumber())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .address(patientRequest.getAddress())
                .build();
    }

    public List<PatientResponse> toResponseList(List<Patient> patients) {
        if (patients == null || patients.isEmpty()) return List.of();

        return patients.stream()
                .map(this::toResponse)
                .toList();
    }
}
