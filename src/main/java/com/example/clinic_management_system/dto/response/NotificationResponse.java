package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class NotificationResponse {
    private String notificationId;
    private String type;
    private String title;
    private String message;
    private boolean isRead;
    private Instant createdAt;
}
