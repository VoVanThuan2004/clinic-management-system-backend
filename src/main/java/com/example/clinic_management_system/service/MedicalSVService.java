package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicalServiceRequest;
import com.example.clinic_management_system.dto.response.MedicalServiceOptionResponse;
import com.example.clinic_management_system.dto.response.MedicalServiceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MedicalSVService {
    void addService(MedicalServiceRequest medicalServiceRequest);

    void updateService(String serviceId, MedicalServiceRequest medicalServiceRequest);

    void deleteService(String serviceId);

    List<MedicalServiceOptionResponse> getServiceOptions();

    Page<MedicalServiceResponse> findAllServices(int page, int size, String search);
}
