package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.PatientRequest;
import com.example.clinic_management_system.dto.response.PatientResponse;
import com.example.clinic_management_system.entity.Patient;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.PatientMapper;
import com.example.clinic_management_system.repository.PatientRepository;
import com.example.clinic_management_system.utils.PatientCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;


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
        patient.setPatientCode(PatientCodeUtil.generatePatientCode());

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

        // 2. Xóa bệnh nhân
        patientRepository.delete(patientOptional.get());
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
        if (ids.isEmpty()) {
            throw new BadRequestException("Danh sách ids đang trống");
        }

        // Xóa danh sách bệnh nhân
        patientRepository.deleteAllById(ids);
    }
}
