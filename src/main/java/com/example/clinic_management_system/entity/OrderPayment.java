package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "order_payment")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    private String serviceName;
    private Double serviceFee;
    private Double totalMedicine;
    private Double totalAmount;
    private boolean paymentStatus;
    private String paymentMethod;
    private Instant paidAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicalRecordId")
    private MedicalRecord medicalRecord;
}
