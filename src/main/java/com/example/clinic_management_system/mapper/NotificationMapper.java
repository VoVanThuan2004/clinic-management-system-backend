package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.response.NotificationResponse;
import com.example.clinic_management_system.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .appointmentTime(notification.getAppointmentTime())
                .build();
    }

    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toResponse)
                .toList();
    }
}
