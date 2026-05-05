package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("""
        select r
        from Room r
        order by r.createdAt desc
    """)
    List<Room> findAllRooms();

    @Query("""
        select new com.example.clinic_management_system.dto.response.RoomOptionResponse(r.roomId, r.roomName)
        from Room r
        where r.isActive = true
        order by r.createdAt desc
    """)
    List<RoomOptionResponse> findAllOptions();
}
