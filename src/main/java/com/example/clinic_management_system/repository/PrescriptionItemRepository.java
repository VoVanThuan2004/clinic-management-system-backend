package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, String> {

    // Kiểm tra thuốc đã có trong toa chưa
    @Query("""
        select pi
        from PrescriptionItem pi
        where pi.prescription.prescriptionId = :prescriptionId and pi.medicine.medicineId = :medicineId
    """)
    Optional<PrescriptionItem> findByPrescriptionIdAndMedicineId(
            @Param("prescriptionId") String prescriptionId,
            @Param("medicineId") String medicineId
    );
}
