package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.dto.response.RoomOptionResponse;
import com.example.clinic_management_system.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("""
        select r
        from Room r
        where r.roomName ilike concat('%', :search ,'%')
    """)
    Page<Room> findAllRooms(@Param("search") String search, Pageable pageable);

    @Query("""
        select new com.example.clinic_management_system.dto.response.RoomOptionResponse(r.roomId, r.roomName)
        from Room r
        where r.isActive = true
        order by r.createdAt desc
    """)
    List<RoomOptionResponse> findAllOptions();
}
