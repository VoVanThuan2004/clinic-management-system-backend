package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.PrescriptionItemRequest;
import com.example.clinic_management_system.dto.response.PrescriptionItemResponse;
import com.example.clinic_management_system.entity.Medicine;
import com.example.clinic_management_system.entity.Prescription;
import com.example.clinic_management_system.entity.PrescriptionItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PrescriptionItemMapper {

    public PrescriptionItemResponse toResponse(PrescriptionItem prescriptionItem) {
        return PrescriptionItemResponse.builder()
                .itemId(prescriptionItem.getItemId())
                .medicineName(prescriptionItem.getMedicineName())
                .price(prescriptionItem.getPrice())
                .quantity(prescriptionItem.getQuantity())
                .dosage(prescriptionItem.getDosage())
                .imageUrl(prescriptionItem.getImageUrl())
                .build();
    }

    public List<PrescriptionItemResponse> toResponseList(List<PrescriptionItem> prescriptionItemList) {
        return prescriptionItemList.stream()
                .map(this::toResponse)
                .toList();
    }

    public PrescriptionItem toEntity(PrescriptionItemRequest prescriptionItemRequest, Medicine medicine, Prescription prescription) {
        return PrescriptionItem.builder()
                .prescription(prescription)
                .medicine(medicine)
                .medicineName(medicine.getMedicineName())
                .price(medicine.getSellingPrice())
                .quantity(prescriptionItemRequest.getQuantity())
                .imageUrl(medicine.getImage())
                .build();
    }
}
