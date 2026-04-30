package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.NotificationResponse;
import com.example.clinic_management_system.entity.Notification;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.NotificationMapper;
import com.example.clinic_management_system.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Override
    public Page<NotificationResponse> getAllNotifications(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Query data
        Page<Notification> notifications = notificationRepository.findAllByUserId(userId, pageable);

        // Mapping data trả về
        Page<NotificationResponse> notificationResponses = notifications.map(notificationMapper::toResponse);

        return notificationResponses;
    }

    @Override
    public void markReadNotification(String id) {
        // 1. Kiểm tra notification này có tồn tại
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isEmpty()) {
            throw new ResourceNotFoundException("Thông báo không tồn tại");
        }

        notification.get().setRead(true);
        notificationRepository.save(notification.get());
    }
}
