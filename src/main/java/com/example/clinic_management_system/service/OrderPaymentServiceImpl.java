package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.OrderPaymentRequest;
import com.example.clinic_management_system.dto.response.OrderPaymentResponse;
import com.example.clinic_management_system.entity.*;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.OrderPaymentMapper;
import com.example.clinic_management_system.repository.AppointmentRepository;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.MedicineRepository;
import com.example.clinic_management_system.repository.OrderPaymentRepository;
import com.example.clinic_management_system.utils.AppointmentStatusConstants;
import com.example.clinic_management_system.utils.PaymentMethodConstant;
import com.example.clinic_management_system.utils.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final OrderPaymentMapper orderPaymentMapper;
    private final MedicineRepository medicineRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public String createOrderPayment(OrderPaymentRequest orderPaymentRequest) {
        // 1. Kiểm tra hồ sơ bệnh lý có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(orderPaymentRequest.getMedicalRecordId());
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // Kiểm tra tổng tiền thực tế có đúng hay không
        Double totalMedicine = medicalRecord.get().getPrescription().getPrescriptionItems()
                .stream()
                .mapToDouble(item -> {
                    return item.getPrice() * item.getQuantity();
                })
                .sum();
        Double totalPrice = totalMedicine + medicalRecord.get().getAppointment().getMedicalServiceEntity().getPrice();
        if (!orderPaymentRequest.getTotalAmount().equals(totalPrice)) {
            throw new BadRequestException("Tổng tiền thanh toán không hợp lệ");
        }

        // Trừ tồn kho
        if (orderPaymentRequest.getPaymentMethod().equals(PaymentMethodConstant.CASH)) {
            List<PrescriptionItem> items = medicalRecord.get().getPrescription().getPrescriptionItems();
            for (PrescriptionItem item: items) {
                int currentStock = item.getMedicine().getStockQuantity();
                int quantity = item.getQuantity();
                if (currentStock < quantity) {
                    throw new BadRequestException("Thuốc " + item.getMedicine().getMedicineName() + " trong hồ sơ bệnh lý đã hết hàng");
                }
                item.getMedicine().setStockQuantity(currentStock - quantity);
                medicineRepository.save(item.getMedicine());
            }
        }


        // 2. Mapping data
        OrderPayment orderPayment = orderPaymentMapper.toEntity(orderPaymentRequest);
//        orderPayment.setMedicalRecord(medicalRecord.get());
//        orderPayment.setServiceName(medicalRecord.get().getAppointment().getMedicalServiceEntity().getServiceName());

        OrderPayment currentOrderPayment = medicalRecord.get().getOrderPayment() != null ? medicalRecord.get().getOrderPayment() : orderPayment;
        currentOrderPayment.setMedicalRecord(medicalRecord.get());
        currentOrderPayment.setServiceName(medicalRecord.get().getAppointment().getMedicalServiceEntity().getServiceName());

        // Cập nhật trạng thái hồ sơ bệnh lý
        if (orderPaymentRequest.getPaymentMethod().equals(PaymentMethodConstant.CASH)) {
            Appointment appointment = medicalRecord.get().getAppointment();
            appointment.setStatus(AppointmentStatusConstants.COMPLETED);
            appointmentRepository.save(appointment);

            medicalRecord.get().setPaymentStatus(true);
            medicalRecordRepository.save(medicalRecord.get());

            currentOrderPayment.setStatus(PaymentStatus.SUCCESS);
            currentOrderPayment.setPaidAt(Instant.now());
        }
        //  Thanh toán chuyển khoản
        else {
            currentOrderPayment.setStatus(PaymentStatus.PENDING);
        }

        orderPaymentRepository.save(currentOrderPayment);
        return currentOrderPayment.getOrderId();
    }

    @Override
    public OrderPaymentResponse getOrderPayment(String recordId) {
        // 1. Kiểm tra hóa đơn có tồn tại
        Optional<OrderPayment> orderPayment = orderPaymentRepository.findByMedicalRecord_MedicalRecordId(recordId);
        if (orderPayment.isEmpty()) {
            throw new ResourceNotFoundException("Hóa đơn thanh toán không tồn tại");
        }

        // 2. Mapping data trả về
        return orderPaymentMapper.toResponse(orderPayment.get());
    }
}
