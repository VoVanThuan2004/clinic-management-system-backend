package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<NotificationResponse> getAllNotifications(int page, int size, String userId);

    void markReadNotification(String id);
}
