package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {

    @Query("""
        select mr
        from MedicalRecord mr
        where (:doctorId is null or mr.appointment.doctor.userId = :doctorId)
        and (
            :search is null
            or mr.appointment.patient.fullName ilike concat('%', :search, '%')
        )
        and (:paymentStatus is null or mr.paymentStatus = :paymentStatus)
    """)
    Page<MedicalRecord> findAllMedicalRecords(
            Pageable pageable,
            @Param("search") String search,
            @Param("doctorId") String doctorId,
            @Param("paymentStatus") boolean paymentStatus
    );


    @Query("""
        select mr
        from MedicalRecord mr
        join Appointment a on a.appointmentId = mr.appointment.appointmentId
        where a.patient.patientId = :patientId and mr.orderPayment.paymentStatus = true
    """)
    Page<MedicalRecord> findAllByPatientId(@Param("patientId") String patientId, Pageable pageable);

    Optional<MedicalRecord> findByAppointment_AppointmentId(String appointmentId);
}
