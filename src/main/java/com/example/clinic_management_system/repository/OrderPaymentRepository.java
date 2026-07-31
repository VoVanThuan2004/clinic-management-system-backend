package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, String> {
    Optional<OrderPayment> findByMedicalRecord_MedicalRecordId(String recordId);
}
