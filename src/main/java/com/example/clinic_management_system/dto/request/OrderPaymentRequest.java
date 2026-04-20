package com.example.clinic_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class OrderPaymentRequest {
    @NotBlank(message = "Hồ sơ bệnh lý không được để trống")
    private String medicalRecordId;

    @NotNull(message = "Phí dịch vụ không được để trống")
    @PositiveOrZero(message = "Phí dịch vụ phải lớn hơn hoặc bằng 0")
    private Double serviceFee;

    @NotNull(message = "Tổng tiền thuốc không được để trống")
    @PositiveOrZero(message = "Tổng tiền thuốc phải lớn hơn hoặc bằng 0")
    private Double totalMedicine;

    @NotNull(message = "Tổng thanh toán không được để trống")
    @PositiveOrZero(message = "Tổng thanh toán phải lớn hơn hoặc bằng 0")
    private Double totalAmount;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;
}
