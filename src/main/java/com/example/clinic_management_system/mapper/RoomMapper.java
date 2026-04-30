package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.response.RoomResponse;
import com.example.clinic_management_system.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    public List<RoomResponse> toResponseList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toResponse)
                .toList();
    }
}
