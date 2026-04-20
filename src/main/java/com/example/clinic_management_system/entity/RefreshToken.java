package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String refreshTokenId;

    @Column(name = "refresh_token", length = 1000, nullable = false)
    private String refreshToken;

    private String userAgent;
    private String ipAddress;

    @CreationTimestamp
    private Instant createdAt;

    private Instant expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
