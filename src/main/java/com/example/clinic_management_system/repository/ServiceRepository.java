package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.MedicalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<MedicalServiceEntity, String> {
}
