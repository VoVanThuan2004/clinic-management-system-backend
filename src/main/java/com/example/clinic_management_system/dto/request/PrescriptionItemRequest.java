package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionItemRequest {
    @NotBlank(message = "prescriptionId không được để trống")
    private String prescriptionId;

    @NotBlank(message = "medicineId không được để trống")
    private String medicineId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}
