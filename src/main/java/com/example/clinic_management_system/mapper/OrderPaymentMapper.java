package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import com.example.clinic_management_system.entity.OrderPayment;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrderPaymentMapper {

    public OrderPayment toEntity(OrderPaymentRequest orderPaymentRequest) {
        return OrderPayment.builder()
                .serviceFee(orderPaymentRequest.getServiceFee())
                .totalMedicine(orderPaymentRequest.getTotalMedicine())
                .totalAmount(orderPaymentRequest.getTotalAmount())
                .paymentMethod(orderPaymentRequest.getPaymentMethod())
                .paymentStatus(orderPaymentRequest.getPaymentMethod().equals("cash"))
                .paidAt(orderPaymentRequest.getPaymentMethod().equals("cash") ? Instant.now() : null)
                .build();
    }

    public OrderPaymentResponse toResponse(OrderPayment orderPayment) {
        return OrderPaymentResponse.builder()
                .orderId(orderPayment.getOrderId())
                .serviceFee(orderPayment.getServiceFee())
                .totalMedicine(orderPayment.getTotalMedicine())
                .totalAmount(orderPayment.getTotalAmount())
                .paymentStatus(orderPayment.isPaymentStatus())
                .paymentMethod(orderPayment.getPaymentMethod())
                .build();
    }
}
