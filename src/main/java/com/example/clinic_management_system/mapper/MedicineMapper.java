package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.MedicineRequest;
import com.example.clinic_management_system.dto.response.MedicineResponse;
import com.example.clinic_management_system.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine toEntity(MedicineRequest medicineRequest) {
        return Medicine.builder()
                .medicineName(medicineRequest.getMedicineName())
                .sellingPrice(medicineRequest.getSellingPrice())
                .originalPrice(medicineRequest.getOriginalPrice())
                .stockQuantity(medicineRequest.getStockQuantity())
                .unit(medicineRequest.getUnit())
                .description(medicineRequest.getDescription())
                .status(true)
                .build();
    }

    public MedicineResponse toResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .categoryId(medicine.getCategory().getCategoryId())
                .medicineId(medicine.getMedicineId())
                .medicineName(medicine.getMedicineName())
                .sellingPrice(medicine.getSellingPrice())
                .originalPrice(medicine.getOriginalPrice())
                .stockQuantity(medicine.getStockQuantity())
                .image(medicine.getImage())
                .unit(medicine.getUnit())
                .description(medicine.getDescription())
                .status(medicine.isStatus())
                .build();
    }
}
