package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import com.example.clinic_management_system.service.OrderPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-payments")
public class OrderPaymentController {
    private final OrderPaymentService orderPaymentService;

    // Tạo hóa đơn thanh toán
    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> createOrderPayment(@Valid @RequestBody OrderPaymentRequest orderPaymentRequest) {
        String orderId = orderPaymentService.createOrderPayment(orderPaymentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<String>builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .data(orderId)
                        .message("Tạo hóa đơn thanh toán thành công")
                .build());
    }

    // Lấy thông tin chi tiết hóa đơn
    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<OrderPaymentResponse>> getOrderPayment(@PathVariable String recordId) {
        OrderPaymentResponse orderPaymentResponse = orderPaymentService.getOrderPayment(recordId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<OrderPaymentResponse>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin chi tiết hóa đơn thành công")
                        .data(orderPaymentResponse)
                .build());
    }
}
