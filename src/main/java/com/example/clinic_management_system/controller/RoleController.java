package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RoleResponse;
import com.example.clinic_management_system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllRoles() {
        // Gọi service
        List<RoleResponse> roles = roleService.getAllRoles();
        return ResponseEntity.status(200).body(ApiResponse.<List<RoleResponse>>builder()
                        .status("success")
                        .code(200)
                        .message("Danh sách vai trò")
                        .data(roles)
                .build());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable String roleId) {
        RoleResponse role = roleService.getRoleById(roleId);
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy chi tiết vai trò")
                .data(role)
                .build();
        return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);
    }
}
