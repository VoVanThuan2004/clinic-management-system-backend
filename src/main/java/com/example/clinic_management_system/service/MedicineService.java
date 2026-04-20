package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.MedicineRequest;
import com.example.clinic_management_system.dto.response.MedicineResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface MedicineService {
    void addMedicine(MedicineRequest medicineRequest, MultipartFile file);

    void updateMedicine(String medicineId, MedicineRequest medicineRequest, MultipartFile file);

    void deleteMedicine(String medicineId);

    Page<MedicineResponse> getMedicinesByCategory(String categoryId, int page, int size);
}
