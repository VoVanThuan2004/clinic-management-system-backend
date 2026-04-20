package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, String> {
}
