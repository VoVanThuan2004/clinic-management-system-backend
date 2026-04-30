package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.entity.OrderPayment;
import com.example.clinic_management_system.entity.PrescriptionItem;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.OrderPaymentMapper;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.MedicineRepository;
import com.example.clinic_management_system.repository.OrderPaymentRepository;
import com.example.clinic_management_system.repository.PrescriptionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final OrderPaymentMapper orderPaymentMapper;
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public String createOrderPayment(OrderPaymentRequest orderPaymentRequest) {
        // 1. Kiểm tra hồ sơ bệnh lý có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(orderPaymentRequest.getMedicalRecordId());
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // Trừ tồn kho
        if (orderPaymentRequest.getPaymentMethod().equals("cash")) {
            List<PrescriptionItem> items = medicalRecord.get().getPrescription().getPrescriptionItems();
            for (PrescriptionItem item: items) {
                int currentStock = item.getMedicine().getStockQuantity();
                int quantity = item.getQuantity();
                if (currentStock < quantity) {
                    throw new BadRequestException("Không đủ thuốc trong kho");
                }
                item.getMedicine().setStockQuantity(currentStock - quantity);
                medicineRepository.save(item.getMedicine());
            }
        }


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
