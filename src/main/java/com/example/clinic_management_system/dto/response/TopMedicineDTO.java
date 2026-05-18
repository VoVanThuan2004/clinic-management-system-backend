package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class TopMedicineDTO {
    private String medicineId;
    private String medicineName;
    private Long totalSold;
}
