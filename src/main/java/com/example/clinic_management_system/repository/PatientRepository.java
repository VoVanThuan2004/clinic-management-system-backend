package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, String> {

    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("""
        select p
        from Patient p
        where (p.fullName like %:search% or p.phoneNumber like %:search%) and p.isDeleted = false
    """)
    Page<Patient> findAllPatientsPagination(Pageable pageable, String search);

    List<Patient> findAllByPhoneNumberInAndDeletedFalse(List<String> phoneNumbers);

    @Query("""
        select p
        from Patient p
        where (p.fullName like %:search% or p.phoneNumber like %:search%) and p.isDeleted = false
        order by p.createdAt desc
    """)
    List<Patient> findAllPatientsBySearch(String search);
}
