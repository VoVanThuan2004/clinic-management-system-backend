package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PrescriptionItemRequest;
import com.example.clinic_management_system.entity.Medicine;
import com.example.clinic_management_system.entity.Prescription;
import com.example.clinic_management_system.entity.PrescriptionItem;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.PrescriptionItemMapper;
import com.example.clinic_management_system.repository.MedicineRepository;
import com.example.clinic_management_system.repository.PrescriptionItemRepository;
import com.example.clinic_management_system.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionItemMapper prescriptionItemMapper;

    @Override
    public void addItem(PrescriptionItemRequest prescriptionItemRequest) {
        // 1. Kiểm tra toa thuốc có hợp lệ
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionItemRequest.getPrescriptionId());
        if (prescription.isEmpty()) {
            throw new ResourceNotFoundException("Toa thuốc không tồn tại");
        }

        // 2. Kiểm tra thuốc có hợp lệ
        Optional<Medicine> medicine = medicineRepository.findById(prescriptionItemRequest.getMedicineId());
        if (medicine.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc không tồn tại");
        }

        // 3. Thêm thuốc vào item
        PrescriptionItem prescriptionItem = prescriptionItemMapper.toEntity(prescriptionItemRequest, medicine.get(), prescription.get());
        prescriptionItemRepository.save(prescriptionItem);
    }

    @Override
    public void updateQuantity(Integer newQuantity, String itemId) {
        // 1. Kiểm tra item có tồn tại
        Optional<PrescriptionItem> prescriptionItem = prescriptionItemRepository.findById(itemId);
        if (prescriptionItem.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc trong toa không tồn tại");
        }

        // 2. Kiểm tra số lượng mới
        if (newQuantity < 1) {
            throw new BadRequestException("Số lượng phải lớn hơn bằng 1");
        }

        // 3. Cập nhật số lượng
        prescriptionItem.get().setQuantity(newQuantity);
        prescriptionItemRepository.save(prescriptionItem.get());
    }

    @Override
    public void updateDosage(String dosage, String itemId) {
        // 1. Kiểm tra item có tồn tại
        Optional<PrescriptionItem> prescriptionItem = prescriptionItemRepository.findById(itemId);
        if (prescriptionItem.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc trong toa không tồn tại");
        }

        // 2. Cập nhật liều lượng
        prescriptionItem.get().setDosage(dosage);
        prescriptionItemRepository.save(prescriptionItem.get());
    }

    @Override
    public void deleteItem(String itemId) {
        // 1. Kiểm tra item có tồn tại
        Optional<PrescriptionItem> prescriptionItem = prescriptionItemRepository.findById(itemId);
        if (prescriptionItem.isEmpty()) {
            throw new ResourceNotFoundException("Thuốc trong toa không tồn tại");
        }

        // 2. Xóa
        prescriptionItemRepository.delete(prescriptionItem.get());
    }
}
