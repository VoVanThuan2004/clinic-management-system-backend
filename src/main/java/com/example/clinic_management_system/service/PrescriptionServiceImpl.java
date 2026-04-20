package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PrescriptionRequest;
import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.entity.Prescription;
import com.example.clinic_management_system.entity.PrescriptionItem;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.PrescriptionMapper;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public void createPrescription(PrescriptionRequest prescriptionRequest) {
        // 1. Kiểm tra hồ sơ bệnh lý có hợp lệ
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(prescriptionRequest.getMedicalRecordId());
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // 2. Tạo toa thuốc
        prescriptionRepository.save(Prescription.builder()
                        .medicalRecord(medicalRecord.get())
                .build());
    }

    @Override
    @Transactional
    public PrescriptionResponse getPrescription(String recordId) {
        // 1. Kiểm tra hồ sơ bệnh lý có hợp lệ
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // 2. Lấy thông tin toa thuốc
        Prescription prescription = prescriptionRepository.findByMedicalRecordId(recordId);

        // 3. Mapper data trả về
        PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);
        prescriptionResponse.setServiceName(medicalRecord.get().getAppointment().getMedicalServiceEntity().getServiceName());
        prescriptionResponse.setServiceFee(medicalRecord.get().getAppointment().getMedicalServiceEntity().getPrice());
        return prescriptionResponse;
    }
}
