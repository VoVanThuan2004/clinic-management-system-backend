package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.dto.response.MedicalServiceOptionResponse;
import com.example.clinic_management_system.entity.MedicalServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalServiceEntity, String> {

    @Query("""
        select new com.example.clinic_management_system.dto.response.MedicalServiceOptionResponse(m.serviceId, m.serviceName)
        from MedicalServiceEntity m
        order by m.createdAt desc
    """)
    List<MedicalServiceOptionResponse> findAllServiceOptions();


    @Query("""
        select s
        from MedicalServiceEntity s
        where s.serviceName ilike concat('%', :search, '%')
    """)
    Page<MedicalServiceEntity> findAllServices(
            @Param("search") String search,
            Pageable pageable
    );
}
