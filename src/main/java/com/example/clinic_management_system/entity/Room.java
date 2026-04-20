package com.example.clinic_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter @Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roomId;

    @Column(nullable = false)
    private String roomName;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
