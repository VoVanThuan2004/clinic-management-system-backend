package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.entity.OrderPayment;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.OrderPaymentMapper;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final OrderPaymentMapper orderPaymentMapper;

    @Override
    public String createOrderPayment(OrderPaymentRequest orderPaymentRequest) {
        // 1. Kiểm tra hồ sơ bệnh lý có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(orderPaymentRequest.getMedicalRecordId());
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // Trừ tồn kho


        // 2. Mapping data
        OrderPayment orderPayment = orderPaymentMapper.toEntity(orderPaymentRequest);
        orderPayment.setMedicalRecord(medicalRecord.get());
        orderPayment = orderPaymentRepository.save(orderPayment);

        // Cập nhật trạng thái hồ sơ bệnh lý
        if (orderPaymentRequest.getPaymentMethod().equals("cash")) {
            medicalRecord.get().setPaymentStatus(true);
            medicalRecordRepository.save(medicalRecord.get());
        }

        return orderPayment.getOrderId();
    }

    @Override
    public OrderPaymentResponse getOrderPayment(String orderId) {
        // 1. Kiểm tra hóa đơn có tồn tại
        Optional<OrderPayment> orderPayment = orderPaymentRepository.findById(orderId);
        if (orderPayment.isEmpty()) {
            throw new ResourceNotFoundException("Hóa đơn thanh toán không tồn tại");
        }

        // 2. Mapping data trả về
        OrderPaymentResponse orderPaymentResponse = orderPaymentMapper.toResponse(orderPayment.get());

        return orderPaymentResponse;
    }
}
