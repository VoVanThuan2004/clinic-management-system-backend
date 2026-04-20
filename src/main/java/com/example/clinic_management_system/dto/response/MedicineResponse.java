package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class MedicineResponse {
    private String medicineId;
    private String medicineName;
    private Double sellingPrice;
    private Double originalPrice;
    private Integer stockQuantity;
    private String image;
    private String unit;
    private String description;
    private boolean status;
}
