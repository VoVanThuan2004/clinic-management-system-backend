package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.RefreshToken;
import com.example.clinic_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
