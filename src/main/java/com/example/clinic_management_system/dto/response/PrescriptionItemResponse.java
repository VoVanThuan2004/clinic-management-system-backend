package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor
@NoArgsConstructor
public class PrescriptionItemResponse {
    private String itemId;
    private String medicineName;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private String dosage;
}
