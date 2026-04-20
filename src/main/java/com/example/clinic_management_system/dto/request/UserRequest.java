package com.example.clinic_management_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private String roleId;
    private String email;
    private String fullName;
    private String password;
    private String phoneNumber;
    private int gender;
    private DoctorDetailRequest doctorDetailRequest;
}
