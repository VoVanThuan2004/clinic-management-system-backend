package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import jakarta.validation.Valid;

public interface OrderPaymentService {
    String createOrderPayment(@Valid OrderPaymentRequest orderPaymentRequest);

    OrderPaymentResponse getOrderPayment(String recordId);
}
