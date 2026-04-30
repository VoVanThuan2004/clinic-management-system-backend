package com.example.clinic_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    // Định nghĩa hàm gửi thông báo cho bác sĩ
    public void sendToDoctor(String doctorId, Object payload) {
        messagingTemplate.convertAndSendToUser(doctorId, "/queue/notifications", payload);
    }
}
