package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RoleResponse;
import com.example.clinic_management_system.entity.Role;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        // Lấy danh sách roles trừ admin
        List<Role> roles = roleRepository.findAllExcludeName("ADMIN");

        List<RoleResponse> roleResponses = roles.stream()
                .map(role -> RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .name(role.getName())
                        .build())
                .toList();

        return roleResponses;
    }

    @Override
    public RoleResponse getRoleById(String id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new ResourceNotFoundException("Vai trò không tồn tại");
        }
        return RoleResponse.builder()
                .roleId(role.get().getRoleId())
                .name(role.get().getName())
                .build();
    }


}
