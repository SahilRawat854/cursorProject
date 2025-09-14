package com.spingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Message is required")
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required")
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime readAt;
    
    // Constructors
    public Notification() {}
    
    public Notification(User user, String title, String message, NotificationType type) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    // Enums
    public enum NotificationType {
        BOOKING_CONFIRMED, BOOKING_CANCELLED, PAYMENT_SUCCESS, PAYMENT_FAILED,
        BIKE_AVAILABLE, BIKE_MAINTENANCE, RIDE_REMINDER, REVIEW_REQUEST,
        PROMOTION, SYSTEM_UPDATE
    }
    
    public enum NotificationStatus {
        UNREAD, READ, ARCHIVED
    }
}
