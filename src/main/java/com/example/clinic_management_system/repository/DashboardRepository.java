package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface DashboardRepository extends JpaRepository<MedicalRecord, String> {


    @Query("""
        select
            count(distinct a.patient.patientId),
    
            coalesce(sum(a.medicalServiceEntity.price), 0) +
    
            coalesce((
                select sum(pi.price * pi.quantity)
                from PrescriptionItem pi
                where pi.prescription.medicalRecord.appointment.startTime >= :start
                  and pi.prescription.medicalRecord.appointment.startTime <= :end
                  and pi.prescription.medicalRecord.appointment.status = 'completed'
            ), 0)
    
        from MedicalRecord mr
        join mr.appointment a
        where a.startTime >= :start
          and a.startTime <= :end
          and a.status = 'completed'
    """)
    Object[] getTodayStatisticsRaw(
            @Param("start") Instant start,
            @Param("end") Instant end
    );


    @Query("""
        select
            date_trunc(:groupBy, op.paidAt),
            COALESCE(SUM(op.totalAmount), 0)
        from OrderPayment op
        where op.paymentStatus = true
        and op.paidAt between :start and :end
        group by date_trunc(:groupBy, op.paidAt)
        order by date_trunc(:groupBy, op.paidAt)
    """)
    List<Object[]> getRevenueStats(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("groupBy") String groupBy
    );


    @Query("""
        select
            date_trunc(:groupBy, op.paidAt),
            COALESCE(SUM(
                (m.sellingPrice - m.originalPrice + op.serviceFee) * pi.quantity
            ), 0)
        from OrderPayment op
        join MedicalRecord mr on op.medicalRecord.medicalRecordId = mr.medicalRecordId
        join Prescription p on mr.prescription.prescriptionId = p.prescriptionId
        join PrescriptionItem pi on p.prescriptionId = pi.prescription.prescriptionId
        join Medicine m on pi.medicine.medicineId = m.medicineId
        where op.paymentStatus = true
        and op.paidAt between :start and :end
        group by date_trunc(:groupBy, op.paidAt)
        order by date_trunc(:groupBy, op.paidAt)
    """)
    List<Object[]> getProfitStats(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("groupBy") String groupBy
    );
}
