package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.DoctorUpdateRequest;
import com.example.clinic_management_system.dto.request.UserRequest;
import com.example.clinic_management_system.dto.request.EmployeeUpdateRequest;
import com.example.clinic_management_system.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void createUser(UserRequest userRequest);

    UserResponse getProfile(String userId);

    Page<UserResponse> getAllEmployees(int page, int size, String search);

    void updateEmployee(String id, EmployeeUpdateRequest employeeUpdateRequest, MultipartFile file);

    void updateDoctor(String id, DoctorUpdateRequest doctorUpdateRequest, MultipartFile file);
}
