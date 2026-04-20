package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.RoomRequest;
import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    List<RoomResponse> getAllRooms();

    List<RoomOptionResponse> getRoomOptions();

    void addRoom(RoomRequest roomRequest);

    void updateRoom(String roomId, RoomRequest roomRequest);

    void deleteRoom(String roomId);

    void updateIsActiveRoom(String roomId);
}
