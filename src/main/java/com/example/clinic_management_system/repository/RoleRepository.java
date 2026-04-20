package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("""
        select r
        from Role r
        where r.name != :name
    """)
    List<Role> findAllExcludeName(@Param("name") String name);

    Optional<Role> findRoleByName(String name);

    boolean existsByRoleId(String roleId);

    boolean existsByRoleIdAndName(String roleId, String name);
}
