package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    @Query("""
        select a
        from Appointment a
        where (:date is null or cast(a.startTime as date) = :date)
        and (:doctorId is null or a.doctor.userId = :doctorId)
        and (
            :search is null
            or a.patient.fullName ilike concat('%', :search, '%')
            or a.patient.phoneNumber ilike concat('%', :search, '%')
        )
    """)
    Page<Appointment> findAllAppointments(
            Pageable pageable,
            @Param("search") String search,
            @Param("date") LocalDate date,
            @Param("doctorId") String doctorId
    );
}
