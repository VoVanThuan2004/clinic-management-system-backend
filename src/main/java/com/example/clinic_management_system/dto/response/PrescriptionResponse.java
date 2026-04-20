package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class PrescriptionResponse {
    private String prescriptionId;
    private Instant createdAt;
    private List<PrescriptionItemResponse> items;
    private String serviceName;
    private Double serviceFee;
}
