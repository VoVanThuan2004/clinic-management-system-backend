package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.request.RoomRequest;
import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.dto.response.RoomResponse;
import com.example.clinic_management_system.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<RoomResponse>>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<RoomResponse> roomResponses = roomService.getAllRooms(page, size, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<RoomResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách phòng khám")
                        .data(roomResponses)
                .build());
    }

    @GetMapping("/options")
    public ResponseEntity<ApiResponse<List<RoomOptionResponse>>> getRoomOptions() {
        List<RoomOptionResponse> roomOptions = roomService.getRoomOptions();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<RoomOptionResponse>>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách lựa chọn phòng khám")
                .data(roomOptions)
                .build());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addRoom (@RequestBody RoomRequest roomRequest) {
        roomService.addRoom(roomRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.CREATED.value())
                .message("Thêm phòng khám thành công")
                .build());
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateRoom (@PathVariable String roomId, @RequestBody RoomRequest roomRequest) {
        roomService.updateRoom(roomId, roomRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật thông tin phòng khám thành công")
                .build());
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteRoom (@PathVariable String roomId) {
        roomService.deleteRoom(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Xóa phòng khám thành công")
                .build());
    }

    // Cập nhật trạng thái phòng khám
    @PutMapping("/is-active/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> updateIsActiveRoom (@PathVariable String roomId) {
        roomService.updateIsActiveRoom(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Thay đổi trạng thái phòng khám thành công")
                .build());
    }
}
