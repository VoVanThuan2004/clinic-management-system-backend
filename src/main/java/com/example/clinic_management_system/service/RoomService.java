package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.RoomRequest;
import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.dto.response.RoomResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {
    Page<RoomResponse> getAllRooms(int page, int size, String search);

    List<RoomOptionResponse> getRoomOptions();

    void addRoom(RoomRequest roomRequest);

    void updateRoom(String roomId, RoomRequest roomRequest);

    void deleteRoom(String roomId);

    void updateIsActiveRoom(String roomId);
}
