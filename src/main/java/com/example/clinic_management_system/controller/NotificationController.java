package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.NotificationResponse;
import com.example.clinic_management_system.security.CustomUserDetail;
import com.example.clinic_management_system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String userId = ((CustomUserDetail) auth.getPrincipal()).getId();

         Page<NotificationResponse> notificationResponses = notificationService.getAllNotifications(page, size, userId);

         return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<NotificationResponse>>builder()
                         .status("success")
                         .code(HttpStatus.OK.value())
                         .message("Lấy danh sách thông báo thành công")
                         .data(notificationResponses)
                 .build());
    }

    // Đánh dấu đã đọc thông báo
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<?>> markReadNotification(@PathVariable("id") String id) {

        notificationService.markReadNotification(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Đánh dấu thông báo đã đọc")
                .build());
    }
}
