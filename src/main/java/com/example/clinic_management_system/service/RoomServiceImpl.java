package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.RoomRequest;
import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.dto.response.RoomResponse;
import com.example.clinic_management_system.entity.Room;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAllRooms();

        List<RoomResponse> roomResponses = rooms.stream()
                .map(room -> {
                    return RoomResponse.builder()
                            .roomId(room.getRoomId())
                            .roomName(room.getRoomName())
                            .isActive(room.isActive())
                            .createdAt(room.getCreatedAt())
                            .updatedAt(room.getUpdatedAt())
                            .build();
                })
                .toList();
        return roomResponses;
    }

    @Override
    public List<RoomOptionResponse> getRoomOptions() {
        return roomRepository.findAllOptions();
    }

    @Override
    public void addRoom(RoomRequest roomRequest) {
        if (roomRequest.getRoomName() == null) {
            throw new BadRequestException("Vui lòng nhập tên phòng khám");
        }

        roomRepository.save(Room.builder()
                        .roomName(roomRequest.getRoomName())
                .build());
    }

    @Override
    public void updateRoom(String roomId, RoomRequest roomRequest) {
        if (roomRequest.getRoomName() == null) {
            throw new BadRequestException("Vui lòng nhập tên phòng khám");
        }

        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            optionalRoom.get().setRoomName(roomRequest.getRoomName());
            roomRepository.save(optionalRoom.get());
            return;
        }

        throw new ResourceNotFoundException("Phòng khám không tồn tại");
    }

    @Override
    public void deleteRoom(String roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            roomRepository.delete(optionalRoom.get());
            return;
        }

        throw new ResourceNotFoundException("Phòng khám không tồn tại");
    }

    @Override
    public void updateIsActiveRoom(String roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            optionalRoom.get().setActive(!optionalRoom.get().isActive());
            roomRepository.save(optionalRoom.get());
            return;
        }

        throw new ResourceNotFoundException("Phòng khám không tồn tại");
    }
}
