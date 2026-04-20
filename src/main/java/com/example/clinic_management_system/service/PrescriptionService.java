package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PrescriptionRequest;
import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import jakarta.validation.Valid;

public interface PrescriptionService {
    void createPrescription(@Valid PrescriptionRequest prescriptionRequest);

    PrescriptionResponse getPrescription(String recordId);
}
