package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, String> {

    @Query("""
        select m
        from Medicine m
        where (:categoryId is null or m.category.categoryId = :categoryId)
        and (:search is null or m.medicineName LIKE CONCAT('%', :search, '%'))
    """)
    Page<Medicine> findAllByCategoryId(
            @Param("categoryId") String categoryId,
            @Param("search") String search,
            Pageable pageable);


    @Query("""
        select count(m) > 0
        from Medicine m
        where m.category.categoryId = :categoryId
    """)
    boolean existsByCategoryId(@Param("categoryId") String categoryId);
}
