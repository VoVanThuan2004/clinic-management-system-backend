package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "doctor_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDetail {
    @Id
    private String userId; // Sẽ được map chung với userId

    private String specialty;
    private Integer experienceYears;
    private String biography;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // Dùng ID của User làm ID cho DoctorDetail
    @JoinColumn(name = "userId")
    private User user;
}
