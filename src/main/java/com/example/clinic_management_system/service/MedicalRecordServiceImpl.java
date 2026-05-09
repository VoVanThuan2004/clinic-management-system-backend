package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicalRecordRequest;
import com.example.clinic_management_system.dto.request.MedicalRecordUpdateRequest;
import com.example.clinic_management_system.dto.response.MedicalRecordPDFResponse;
import com.example.clinic_management_system.dto.response.MedicalRecordResponse;
import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import com.example.clinic_management_system.entity.Appointment;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.PrescriptionMapper;
import com.example.clinic_management_system.mapper.RecordFileMapper;
import com.example.clinic_management_system.repository.AppointmentRepository;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final RecordFileMapper recordFileMapper;

    @Override
    public String createMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        // 1. Kiểm tra lịch hẹn có hợp lệ
        Optional<Appointment> appointment = appointmentRepository.findById(medicalRecordRequest.getAppointmentId());
        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException("Lịch hẹn không tồn tại");
        }

        // 2. Tạo hồ sơ bệnh lý
        MedicalRecord medicalRecord = medicalRecordRepository.save(MedicalRecord.builder()
                        .appointment(appointment.get())
                        .symptoms(null)
                        .diagnosis(null)
                        .notes(null)
                        .paymentStatus(false)
                .build());

        return medicalRecord.getMedicalRecordId();
    }

    @Override
    public void updateMedicalRecord(String medicalRecordId, MedicalRecordUpdateRequest medicalRecordUpdateRequest) {
        // 1. Kiểm tra hồ sơ bệnh lý có hợp lệ
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // 2. Kiểm tra các field body
        if (medicalRecordUpdateRequest.getSymptoms() == null ||
            medicalRecordUpdateRequest.getDiagnosis() == null || medicalRecordUpdateRequest.getNotes() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // 3. Cập nhật
        medicalRecord.get().setSymptoms(medicalRecordUpdateRequest.getSymptoms());
        medicalRecord.get().setDiagnosis(medicalRecordUpdateRequest.getDiagnosis());
        medicalRecord.get().setNotes(medicalRecordUpdateRequest.getNotes());
        medicalRecordRepository.save(medicalRecord.get());
    }

    @Override
    public Page<MedicalRecordResponse> getMedicalRecords(int page, int size, String search, String doctorId, Boolean paymentStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<MedicalRecord> medicalRecords = medicalRecordRepository.findAllMedicalRecords(pageable, search, doctorId, paymentStatus);

        // Mapping data trả về
        Page<MedicalRecordResponse> medicalRecordResponses = medicalRecords.map(medicalRecord -> {
            return MedicalRecordResponse.builder()
                    .medicalRecordId(medicalRecord.getMedicalRecordId())
                    .doctorName(medicalRecord.getAppointment().getDoctor().getFullName())
                    .patientName(medicalRecord.getAppointment().getPatient().getFullName())
                    .gender(medicalRecord.getAppointment().getPatient().getGender())
                    .phoneNumber(medicalRecord.getAppointment().getPatient().getPhoneNumber())
                    .address(medicalRecord.getAppointment().getPatient().getAddress())
                    .dateOfBirth(medicalRecord.getAppointment().getPatient().getDateOfBirth())
                    .symptoms(medicalRecord.getSymptoms())
                    .diagnosis(medicalRecord.getDiagnosis())
                    .notes(medicalRecord.getNotes())
                    .paymentStatus(medicalRecord.isPaymentStatus())
                    .build();
        });
        return medicalRecordResponses;
    }

    @Override
    @Transactional
    public MedicalRecordPDFResponse getMedicalRecordDetailPDF(String recordId) {
        // 1. Kiểm tra hồ sơ bệnh lý có hợp lệ
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // 2. Mapping data trả về
        PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(medicalRecord.get().getPrescription());
        prescriptionResponse.setServiceName(medicalRecord.get().getAppointment().getMedicalServiceEntity().getServiceName());
        prescriptionResponse.setServiceFee(medicalRecord.get().getAppointment().getMedicalServiceEntity().getPrice());

        MedicalRecordPDFResponse medicalRecordPDFResponse = MedicalRecordPDFResponse.builder()
                .patientName(medicalRecord.get().getAppointment().getPatient().getFullName())
                .gender(medicalRecord.get().getAppointment().getPatient().getGender())
                .phoneNumber(medicalRecord.get().getAppointment().getPatient().getPhoneNumber())
                .dateOfBirth(medicalRecord.get().getAppointment().getPatient().getDateOfBirth())
                .address(medicalRecord.get().getAppointment().getPatient().getAddress())
                .doctorName(medicalRecord.get().getAppointment().getDoctor().getFullName())
                .specialty(medicalRecord.get().getAppointment().getDoctor().getDoctorDetail().getSpecialty())
                .symptoms(medicalRecord.get().getSymptoms())
                .diagnosis(medicalRecord.get().getDiagnosis())
                .notes(medicalRecord.get().getNotes())
                .prescriptions(prescriptionResponse)
                .recordFiles(recordFileMapper.toResponseList(medicalRecord.get().getRecordFiles()))
                .paymentMethod(medicalRecord.get().getOrderPayment().getPaymentMethod())
                .build();

        return medicalRecordPDFResponse;
    }

    @Override
    public MedicalRecordResponse getMedicalRecordDetail(String recordId) {
        // 1. Kiểm tra hồ sơ bệnh lý có hợp lệ
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // 2. Mapping data trả về
        return MedicalRecordResponse.builder()
                .medicalRecordId(medicalRecord.get().getMedicalRecordId())
                .patientName(medicalRecord.get().getAppointment().getPatient().getFullName())
                .gender(medicalRecord.get().getAppointment().getPatient().getGender())
                .phoneNumber(medicalRecord.get().getAppointment().getPatient().getPhoneNumber())
                .dateOfBirth(medicalRecord.get().getAppointment().getPatient().getDateOfBirth())
                .address(medicalRecord.get().getAppointment().getPatient().getAddress())
                .symptoms(medicalRecord.get().getSymptoms())
                .diagnosis(medicalRecord.get().getDiagnosis())
                .notes(medicalRecord.get().getNotes())
                .paymentStatus(medicalRecord.get().isPaymentStatus())
                .build();
    }

    @Override
    public String checkMedicalRecord(String appointmentId) {
        // Query check medical record có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findByAppointment_AppointmentId(appointmentId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        return medicalRecord.get().getMedicalRecordId();
    }
}
