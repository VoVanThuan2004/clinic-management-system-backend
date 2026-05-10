package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("""
        select c
        from Category c
        where c.categoryName ilike concat('%', :search, '%')
    """)
    Page<Category> findAllCategories(@Param("search") String search, Pageable pageable);
}
