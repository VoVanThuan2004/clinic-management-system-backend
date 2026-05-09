package com.example.clinic_management_system.dto.response;

import com.example.clinic_management_system.utils.PaymentMethodConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class MedicalRecordPDFResponse {
    // Thông tin bệnh nhân
    private String patientName;
    private LocalDate dateOfBirth;
    private Integer gender;
    private String phoneNumber;
    private String address;

    // Thông tin bác sĩ
    private String doctorName;
    private String specialty;

    // Thông tin khám
    private String symptoms;
    private String diagnosis;
    private String notes;

    // Thông tin toa thuốc
    private PrescriptionResponse prescriptions;

    // Thông tin file upload
    private List<RecordFileResponse> recordFiles;

    private String paymentMethod;
}
