package com.example.clinic_management_system.dto.response;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MedicalServiceResponse {
    private String serviceId;
    private String serviceName;
    private Double price;
    private String description;
}
