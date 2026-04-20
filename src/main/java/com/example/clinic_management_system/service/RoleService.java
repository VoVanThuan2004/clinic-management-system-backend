package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(String id);
}
