package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "medicine")
@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String medicineId;

    private String medicineName;
    private Double sellingPrice;
    private Double originalPrice;
    private Integer stockQuantity;
    private String publicId;
    private String image;
    private String unit;

    @Column(length = 500)
    private String description;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
