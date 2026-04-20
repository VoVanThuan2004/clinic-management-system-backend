package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
