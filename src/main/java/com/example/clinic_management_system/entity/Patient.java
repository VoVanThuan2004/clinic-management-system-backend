package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Setter @Getter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String patientId;

    private String patientCode;
    private String fullName;
    private Integer gender;

    @Column(length = 10, nullable = false)
    private String phoneNumber;

    private LocalDate dateOfBirth;
    private String address;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
