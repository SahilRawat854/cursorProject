package com.spingo.service;

import com.spingo.entity.Notification;
import com.spingo.entity.User;
import com.spingo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public Notification createNotification(User user, String title, String message, 
                                         Notification.NotificationType type) {
        Notification notification = new Notification(user, title, message, type);
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndStatusOrderByCreatedAtDesc(user, 
            Notification.NotificationStatus.UNREAD);
    }
    
    public Long getUnreadNotificationCount(User user) {
        return notificationRepository.countUnreadNotificationsByUser(user);
    }
    
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setStatus(Notification.NotificationStatus.READ);
        notification.setReadAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }
    
    public void markAllAsRead(User user) {
        List<Notification> unreadNotifications = getUnreadNotifications(user);
        for (Notification notification : unreadNotifications) {
            notification.setStatus(Notification.NotificationStatus.READ);
            notification.setReadAt(LocalDateTime.now());
        }
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    public void deleteOldNotifications(User user, int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<Notification> oldNotifications = notificationRepository
            .findByUserAndCreatedAtAfter(user, cutoffDate);
        
        for (Notification notification : oldNotifications) {
            if (notification.getCreatedAt().isBefore(cutoffDate)) {
                notificationRepository.delete(notification);
            }
        }
    }
    
    // Convenience methods for common notification types
    public Notification createBookingConfirmation(User user, String bikeName) {
        return createNotification(user, "Booking Confirmed", 
            "Your booking for " + bikeName + " has been confirmed.", 
            Notification.NotificationType.BOOKING_CONFIRMED);
    }
    
    public Notification createPaymentSuccess(User user, String amount) {
        return createNotification(user, "Payment Successful", 
            "Payment of ₹" + amount + " has been processed successfully.", 
            Notification.NotificationType.PAYMENT_SUCCESS);
    }
    
    public Notification createRideReminder(User user, String bikeName, LocalDateTime startTime) {
        return createNotification(user, "Ride Reminder", 
            "Your ride with " + bikeName + " starts at " + startTime.toString(), 
            Notification.NotificationType.RIDE_REMINDER);
    }
    
    public Notification createReviewRequest(User user, String bikeName) {
        return createNotification(user, "Review Request", 
            "How was your experience with " + bikeName + "? Please leave a review.", 
            Notification.NotificationType.REVIEW_REQUEST);
    }
}
