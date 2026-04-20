package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PrescriptionItemRequest;
import jakarta.validation.Valid;

public interface PrescriptionItemService {
    void addItem(@Valid PrescriptionItemRequest prescriptionItemRequest);

    void updateQuantity(Integer newQuantity, String itemId);

    void updateDosage(String dosage, String itemId);

    void deleteItem(String itemId);
}
