package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import com.example.clinic_management_system.entity.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionMapper {
    private final PrescriptionItemMapper prescriptionItemMapper;

    public PrescriptionResponse toResponse(Prescription prescription) {
        return PrescriptionResponse.builder()
                .prescriptionId(prescription.getPrescriptionId())
                .createdAt(prescription.getCreatedAt())
                .items(prescriptionItemMapper.toResponseList(prescription.getPrescriptionItems()))
                .build();
    }
}
