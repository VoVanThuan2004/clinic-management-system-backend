package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicineRepository extends JpaRepository<Medicine, String> {

    @Query("""
        select m
        from Medicine m
        where (:categoryId is null or m.category.categoryId = :categoryId)
    """)
    Page<Medicine> findAllByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);
}
