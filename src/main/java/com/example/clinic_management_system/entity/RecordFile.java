package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String fileId;

    private String fileUrl;
    private String fileType;
    private String publicId;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicalRecordId")
    private MedicalRecord medicalRecord;
}
