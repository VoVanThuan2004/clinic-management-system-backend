package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PatientRequest;
import com.example.clinic_management_system.dto.response.MedicalRecordPDFResponse;
import com.example.clinic_management_system.dto.response.PatientResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {
    void addPatient(PatientRequest patientRequest);

    void updatePatient(String patientId, PatientRequest patientRequest);

    void deletePatient(String patientId);

    Page<PatientResponse> getAllPatientsPagination(int page, int size, String search);

    void deletePatients(List<String> ids);

    void addListPatient(List<PatientRequest> patientRequestList);

    List<PatientResponse> getAllPatientsExport(String search);

    Page<MedicalRecordPDFResponse> getPatientHistory(String patientId, int page, int size);
}
