package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
        select u
        from User u
        where u.role.name = 'EMPLOYEE'
        and (u.fullName like %:search% or u.phoneNumber like %:search%)
    """)
    Page<User> findAllEmployees(Pageable pageable, String search);

    @Query("""
        select count(u) > 0
        from User u
        where u.role.name = :roleName
    """)
    boolean existsUserByRoleName(@Param("roleName") String roleName);
}
