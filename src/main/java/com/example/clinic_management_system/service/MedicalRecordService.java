package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicalRecordRequest;
import com.example.clinic_management_system.dto.request.MedicalRecordUpdateRequest;
import com.example.clinic_management_system.dto.response.MedicalRecordPDFResponse;
import com.example.clinic_management_system.dto.response.MedicalRecordResponse;
import org.springframework.data.domain.Page;

public interface MedicalRecordService {
    String createMedicalRecord(MedicalRecordRequest medicalRecordRequest);

    void updateMedicalRecord(String medicalRecordId, MedicalRecordUpdateRequest medicalRecordUpdateRequest);

    Page<MedicalRecordResponse> getMedicalRecords(int page, int size, String search, String doctorId, boolean paymentStatus);

    MedicalRecordPDFResponse getMedicalRecordDetail(String recordId);
}
