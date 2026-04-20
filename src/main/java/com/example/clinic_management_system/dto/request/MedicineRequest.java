package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequest {
    @NotBlank(message = "Category không được để trống")
    private String categoryId;

    @NotBlank(message = "Tên thuốc không được để trống")
    private String medicineName;

    @NotNull(message = "Giá bán không được để trống")
    private Double sellingPrice;

    @NotNull(message = "Giá nhập không được để trống")
    private Double originalPrice;

    @NotNull(message = "Số lượng không được để trống")
    private Integer stockQuantity;

    @NotBlank(message = "Đơn vị không được để trống")
    private String unit;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;
}
