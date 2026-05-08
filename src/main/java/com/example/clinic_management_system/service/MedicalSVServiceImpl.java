package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicalServiceRequest;
import com.example.clinic_management_system.dto.response.MedicalServiceOptionResponse;
import com.example.clinic_management_system.dto.response.MedicalServiceResponse;
import com.example.clinic_management_system.entity.MedicalServiceEntity;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalSVServiceImpl implements MedicalSVService {
    private final MedicalServiceRepository medicalServiceRepository;

    @Override
    public void addService(MedicalServiceRequest medicalServiceRequest) {
        if (medicalServiceRequest.getServiceName() == null || medicalServiceRequest.getPrice() == null ||
            medicalServiceRequest.getDescription() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đủ thông tin");
        }

        // 1. Kiểm tra giá
        if (medicalServiceRequest.getPrice() <= 0) {
            throw new BadRequestException("Giá phải lớn hơn 0");
        }

        // 2. Tạo dịch vụ
        medicalServiceRepository.save(MedicalServiceEntity.builder()
                        .serviceName(medicalServiceRequest.getServiceName())
                        .price(medicalServiceRequest.getPrice())
                        .description(medicalServiceRequest.getDescription())
                .build());
    }

    @Override
    public void updateService(String serviceId, MedicalServiceRequest medicalServiceRequest) {
        if (medicalServiceRequest.getServiceName() == null || medicalServiceRequest.getPrice() == null ||
                medicalServiceRequest.getDescription() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đủ thông tin");
        }

        // 1. Kiểm tra giá
        if (medicalServiceRequest.getPrice() <= 0) {
            throw new BadRequestException("Giá phải lớn hơn 0");
        }

        // 2. Kiểm tra dịch vụ có tồn tại
        Optional<MedicalServiceEntity> medicalServiceEntity = medicalServiceRepository.findById(serviceId);
        if (medicalServiceEntity.isEmpty()) {
            throw new ResourceNotFoundException("Dịch vụ không tồn tại");
        }

        // 3. Cập nhật
        medicalServiceEntity.get().setServiceName(medicalServiceRequest.getServiceName());
        medicalServiceEntity.get().setPrice(medicalServiceRequest.getPrice());
        medicalServiceEntity.get().setDescription(medicalServiceRequest.getDescription());

        medicalServiceRepository.save(medicalServiceEntity.get());
    }

    @Override
    public void deleteService(String serviceId) {
        // 1. Kiểm tra dịch vụ có tồn tại
        Optional<MedicalServiceEntity> medicalServiceEntity = medicalServiceRepository.findById(serviceId);
        if (medicalServiceEntity.isEmpty()) {
            throw new ResourceNotFoundException("Dịch vụ không tồn tại");
        }

        medicalServiceRepository.delete(medicalServiceEntity.get());
    }

    @Override
    public List<MedicalServiceOptionResponse> getServiceOptions() {
        return medicalServiceRepository.findAllServiceOptions();
    }

    @Override
    public Page<MedicalServiceResponse> findAllServices(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<MedicalServiceEntity> medicalServiceEntities = medicalServiceRepository.findAllServices(search, pageable);

        return medicalServiceEntities
                .map(medicalServiceEntity -> {
                    return MedicalServiceResponse.builder()
                            .serviceId(medicalServiceEntity.getServiceId())
                            .serviceName(medicalServiceEntity.getServiceName())
                            .price(medicalServiceEntity.getPrice())
                            .description(medicalServiceEntity.getDescription())
                            .build();
                });
    }
}
