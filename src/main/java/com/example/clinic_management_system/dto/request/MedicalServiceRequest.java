package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalServiceRequest {
    private String serviceName;
    private Double price;
    private String description;
}
