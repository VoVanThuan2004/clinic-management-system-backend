package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {

    @Query("""
        select p
        from Prescription p
        where p.medicalRecord.medicalRecordId = :medicalRecordId
    """)
    Prescription findByMedicalRecordId(@Param("medicalRecordId") String medicalRecordId);
}
