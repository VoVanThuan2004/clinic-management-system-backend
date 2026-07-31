package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderPaymentResponse {
    private String orderId;
    private Double serviceFee;
    private Double totalMedicine;
    private Double totalAmount;
    private boolean paymentStatus;
    private String status;
    private String paymentMethod;
}
