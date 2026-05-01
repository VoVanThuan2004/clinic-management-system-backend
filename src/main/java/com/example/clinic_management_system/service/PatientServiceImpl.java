package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PatientRequest;
import com.example.clinic_management_system.dto.response.MedicalRecordPDFResponse;
import com.example.clinic_management_system.dto.response.PatientResponse;
import com.example.clinic_management_system.dto.response.PrescriptionResponse;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.entity.Patient;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.PatientMapper;
import com.example.clinic_management_system.mapper.PrescriptionMapper;
import com.example.clinic_management_system.mapper.RecordFileMapper;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.PatientRepository;
import com.example.clinic_management_system.utils.PatientCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final RecordFileMapper recordFileMapper;


    @Override
    public void addPatient(PatientRequest patientRequest) {
        // 1. Kiểm tra thông tin
        if (patientRequest.getFullName() == null || patientRequest.getGender() == null
            || patientRequest.getPhoneNumber() == null || patientRequest.getDateOfBirth() == null
                || patientRequest.getAddress() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        if (patientRequest.getGender() != 1 && patientRequest.getGender() != 0) {
            throw new BadRequestException("Vui lòng chọn giới tính");
        }

        // 2. Kiểm tra số điện thoại này đã tồn tại chưa
        Boolean patientExistByPhone = patientRepository.existsByPhoneNumber(patientRequest.getPhoneNumber());
        if (patientExistByPhone) {
            throw new BadRequestException("Số điện thoại này đã tồn tại");
        }

        Patient patient = patientMapper.toEntity(patientRequest);
        patientRepository.save(patient);
    }

    @Override
    public void updatePatient(String patientId, PatientRequest patientRequest) {
        // 1. Kiểm tra thông tin
        if (patientRequest.getFullName() == null || patientRequest.getGender() == null
                || patientRequest.getPhoneNumber() == null || patientRequest.getDateOfBirth() == null
                || patientRequest.getAddress() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // 2. Kiểm tra bệnh nhân có hợp lệ
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bệnh nhân không tồn tại");
        }

        patientOptional.get().setFullName(patientRequest.getFullName());
        patientOptional.get().setGender(patientRequest.getGender());
        patientOptional.get().setPhoneNumber(patientRequest.getPhoneNumber());
        patientOptional.get().setDateOfBirth(patientRequest.getDateOfBirth());
        patientOptional.get().setAddress(patientRequest.getAddress());
        patientRepository.save(patientOptional.get());
    }

    @Override
    public void deletePatient(String patientId) {
        // 1. Kiểm tra bệnh nhân có hợp lệ
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (patientOptional.isEmpty()) {
            throw new ResourceNotFoundException("Bệnh nhân không tồn tại");
        }

        // 2. Xóa bệnh nhân -> xóa mềm
        patientOptional.get().setDeleted(true);
        patientRepository.save(patientOptional.get());
    }

    @Override
    public Page<PatientResponse> getAllPatientsPagination(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Patient> patients = patientRepository.findAllPatientsPagination(pageable, search);
        return patients.map(patientMapper::toResponse);
    }

    @Override
    @Transactional
    public void deletePatients(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BadRequestException("Danh sách ids đang trống");
        }

        List<Patient> patients = patientRepository.findAllById(ids);

        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy bệnh nhân hợp lệ");
        }

        patients.forEach(patient -> patient.setDeleted(true));

        patientRepository.saveAll(patients);
    }

    @Override
    @Transactional
    public void addListPatient(List<PatientRequest> patientRequestList) {
        if (patientRequestList == null || patientRequestList.isEmpty()) {
            throw new BadRequestException("Danh sách bệnh nhân đang trống");
        }

        // 1. Check SĐT trong list có bị trùng nhau
        Set<String> uniquePhones = new HashSet<>();
        for (PatientRequest patientRequest : patientRequestList) {
            if (!uniquePhones.add(patientRequest.getPhoneNumber())) {
                System.out.println(patientRequest.getPhoneNumber());
                throw new BadRequestException(
                        "Số điện thoại bị trùng trong file import: " + patientRequest.getPhoneNumber()
                );
            }
        }

        // 2. Check trùng SĐT trong DB
        List<String> phoneNumbers = patientRequestList.stream()
                .map(PatientRequest::getPhoneNumber)
                .toList();

        List<Patient> existingPatients = patientRepository.findAllByPhoneNumberInAndDeletedFalse(phoneNumbers);
        if (!existingPatients.isEmpty()) {
            String duplicatedPhones = existingPatients.stream()
                    .map(Patient::getPhoneNumber)
                    .distinct()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            throw new BadRequestException(
                    "Số điện thoại đã tồn tại trong hệ thống: " + duplicatedPhones
            );
        }

        // 3. Insert danh sách bệnh nhân
        List<Patient> patientInserts = patientMapper.toEntityList(patientRequestList);
        patientRepository.saveAll(patientInserts);

    }

    @Override
    public List<PatientResponse> getAllPatientsExport(String search) {
        List<Patient> patients = patientRepository.findAllPatientsBySearch(search);

        // Mapping data trả về
        List<PatientResponse> patientResponses = patientMapper.toResponseList(patients);
        return patientResponses;
    }

    @Override
    @Transactional
    public Page<MedicalRecordPDFResponse> getPatientHistory(String patientId, int page, int size) {
        // 1. Check bệnh nhân có tồn tại
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) {
            throw new ResourceNotFoundException("Bệnh nhân không tồn tại");
        }

        // 2. Tạo object phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 3. Query danh sách medical record thuộc patientId
        Page<MedicalRecord> medicalRecords = medicalRecordRepository.findAllByPatientId(patientId, pageable);

        // 4. Mapping data trả về
        Page<MedicalRecordPDFResponse> medicalRecordPDFResponses = medicalRecords
                .map(medicalRecord -> MedicalRecordPDFResponse.builder()
                        .patientName(medicalRecord.getAppointment().getPatient().getFullName())
                        .dateOfBirth(medicalRecord.getAppointment().getPatient().getDateOfBirth())
                        .gender(medicalRecord.getAppointment().getPatient().getGender())
                        .phoneNumber(medicalRecord.getAppointment().getPatient().getPhoneNumber())
                        .address(medicalRecord.getAppointment().getPatient().getAddress())
                        .doctorName(medicalRecord.getAppointment().getDoctor().getFullName())
                        .specialty(medicalRecord.getAppointment().getDoctor().getDoctorDetail().getSpecialty())
                        .symptoms(medicalRecord.getSymptoms())
                        .diagnosis(medicalRecord.getDiagnosis())
                        .notes(medicalRecord.getNotes())
                        .prescriptions(prescriptionMapper.toResponse(medicalRecord.getPrescription()))
                        .recordFiles(recordFileMapper.toResponseList(medicalRecord.getRecordFiles()))
                        .build());

        return medicalRecordPDFResponses;
    }
}
