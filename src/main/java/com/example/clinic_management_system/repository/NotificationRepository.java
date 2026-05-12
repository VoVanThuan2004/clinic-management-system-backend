package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query("""
        select n
        from Notification n
        where n.user.userId = :userId
    """)
    Page<Notification> findAllByUserId(
            @Param("userId") String userId,
            Pageable pageable
    );

    @Query("""
        select count(n)
        from Notification n
        where n.user.userId = :userId and n.isRead = false
    """)
    Long countNotificationByUser(@Param("userId") String userId);


    @Modifying
    @Query("""
        update Notification n
        set n.isRead = true
        where n.user.userId = :userId and n.isRead = false
    """)
    int markAllAsReadByUserId(@Param("userId") String userId);
}
