package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MedicalServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serviceId;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 500)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
