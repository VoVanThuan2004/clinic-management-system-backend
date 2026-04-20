package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, String> {
}
