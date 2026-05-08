package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    // Định nghĩa hàm gửi thông báo cho bác sĩ
    public void sendNotificationToDoctor(String doctorId, NotificationResponse notificationResponse) {
        messagingTemplate.convertAndSend("/topic/notifications/" + doctorId, notificationResponse);
    }
}
