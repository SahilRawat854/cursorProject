package com.spingo.repository;

import com.spingo.entity.Notification;
import com.spingo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    
    List<Notification> findByUserAndStatusOrderByCreatedAtDesc(User user, Notification.NotificationStatus status);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.status = 'UNREAD'")
    Long countUnreadNotificationsByUser(@Param("user") User user);
    
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.createdAt >= :since ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndCreatedAtAfter(@Param("user") User user, @Param("since") LocalDateTime since);
    
    void deleteByUserAndStatus(User user, Notification.NotificationStatus status);
}
