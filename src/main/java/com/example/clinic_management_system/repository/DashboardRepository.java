package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.dto.response.TodayStatisticsDTO;
import com.example.clinic_management_system.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface DashboardRepository extends JpaRepository<MedicalRecord, String> {

    @Query("""
        select count(distinct a.patient.patientId)
        from MedicalRecord mr
        join mr.appointment a
        where a.startTime >= :start
          and a.startTime < :end
          and a.status = 'completed'
    """)
    Long countTodayPatients(
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
        select coalesce(sum(a.medicalServiceEntity.price), 0)
        from MedicalRecord mr
        join mr.appointment a
        where a.startTime >= :start
          and a.startTime < :end
          and a.status = 'completed'
    """)
    BigDecimal getServiceRevenue(
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
    select coalesce(sum(pi.price * pi.quantity), 0)
    from PrescriptionItem pi
    where pi.prescription.medicalRecord.appointment.startTime >= :start
      and pi.prescription.medicalRecord.appointment.startTime < :end
      and pi.prescription.medicalRecord.appointment.status = 'completed'
""")
    BigDecimal getMedicineRevenue(
            @Param("start") Instant start,
            @Param("end") Instant end
    );


    @Query(value = """
        SELECT
            CASE
                WHEN :groupBy = 'day'
                    THEN DATE_FORMAT(op.paid_at, '%Y-%m-%d')
    
                WHEN :groupBy = 'week'
                    THEN CONCAT(YEAR(op.paid_at), '-W', WEEK(op.paid_at, 1))
    
                WHEN :groupBy = 'month'
                    THEN DATE_FORMAT(op.paid_at, '%Y-%m')
    
                WHEN :groupBy = 'quarter'
                    THEN CONCAT(YEAR(op.paid_at), '-Q', QUARTER(op.paid_at))
    
                WHEN :groupBy = 'year'
                    THEN DATE_FORMAT(op.paid_at, '%Y')
            END AS period,
    
            COALESCE(SUM(op.total_amount), 0)
    
        FROM order_payment op
    
        WHERE op.payment_status = true
        AND op.paid_at BETWEEN :start AND :end
    
        GROUP BY period
        ORDER BY period
    """, nativeQuery = true)
    List<Object[]> getRevenueStats(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("groupBy") String groupBy
    );


    @Query(value = """
        SELECT
            period,
            SUM(medicine_profit) + SUM(service_fee) AS total_profit
        FROM (
            SELECT
                op.order_id,
        
                CASE
                    WHEN :groupBy = 'day'
                        THEN DATE_FORMAT(op.paid_at, '%Y-%m-%d')
        
                    WHEN :groupBy = 'week'
                        THEN CONCAT(YEAR(op.paid_at), '-W', WEEK(op.paid_at, 1))
        
                    WHEN :groupBy = 'month'
                        THEN DATE_FORMAT(op.paid_at, '%Y-%m')
        
                    WHEN :groupBy = 'quarter'
                        THEN CONCAT(YEAR(op.paid_at), '-Q', QUARTER(op.paid_at))
        
                    WHEN :groupBy = 'year'
                        THEN DATE_FORMAT(op.paid_at, '%Y')
                END AS period,
        
                SUM(
                    (m.selling_price - m.original_price) * pi.quantity
                ) AS medicine_profit,
        
                op.service_fee AS service_fee
        
            FROM order_payment op
        
            JOIN medical_record mr
                ON op.medical_record_id = mr.medical_record_id
        
            JOIN prescription p
                ON mr.medical_record_id = p.medical_record_id
        
            JOIN prescription_item pi
                ON p.prescription_id = pi.prescription_id
        
            JOIN medicine m
                ON pi.medicine_id = m.medicine_id
        
            WHERE op.payment_status = true
            AND op.paid_at BETWEEN :start AND :end
        
            GROUP BY op.order_id, period, op.service_fee
        ) t
        
        GROUP BY period
        ORDER BY period
    """, nativeQuery = true)
    List<Object[]> getProfitStats(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("groupBy") String groupBy
    );


    @Query(value = """
        SELECT
            m.medicine_id,
            m.medicine_name,
            SUM(pi.quantity) AS total_sold
        FROM order_payment op
        JOIN medical_record mr ON mr.medical_record_id = op.medical_record_id
        JOIN prescription p ON p.medical_record_id = mr.medical_record_id
        JOIN prescription_item pi ON pi.prescription_id = p.prescription_id
        JOIN medicine m ON m.medicine_id = pi.medicine_id
        WHERE op.payment_status = true
        AND op.paid_at BETWEEN :start AND :end
        GROUP BY m.medicine_id, m.medicine_name
        ORDER BY total_sold
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> getTopSellingMedicines(
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("limit") int limit
    );
}
