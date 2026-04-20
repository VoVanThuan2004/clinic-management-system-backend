package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "role")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    private String name;
    private String description;

    // Thời gian tạo, cập nhật
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
