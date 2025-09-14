package com.spingo.controller;

import com.spingo.entity.Notification;
import com.spingo.entity.User;
import com.spingo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public String getNotifications(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationService.getUserNotifications(user);
        Long unreadCount = notificationService.getUnreadNotificationCount(user);
        
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        
        return "notifications/list";
    }
    
    @GetMapping("/unread")
    public String getUnreadNotifications(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationService.getUnreadNotifications(user);
        
        model.addAttribute("notifications", notifications);
        
        return "notifications/unread";
    }
    
    @PostMapping("/mark-read/{notificationId}")
    @ResponseBody
    public String markAsRead(@PathVariable Long notificationId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Notification notification = notificationService.getUserNotifications(user).stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification not found"));
            
            notificationService.markAsRead(notificationId);
            return "success";
        } catch (RuntimeException e) {
            return "error: " + e.getMessage();
        }
    }
    
    @PostMapping("/mark-all-read")
    public String markAllAsRead(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            notificationService.markAllAsRead(user);
            
            redirectAttributes.addFlashAttribute("success", "All notifications marked as read!");
            return "redirect:/notifications";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/notifications";
        }
    }
    
    @PostMapping("/delete/{notificationId}")
    public String deleteNotification(@PathVariable Long notificationId, 
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            Notification notification = notificationService.getUserNotifications(user).stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification not found"));
            
            notificationService.deleteNotification(notificationId);
            
            redirectAttributes.addFlashAttribute("success", "Notification deleted successfully!");
            return "redirect:/notifications";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/notifications";
        }
    }
    
    @GetMapping("/count")
    @ResponseBody
    public Long getUnreadCount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return notificationService.getUnreadNotificationCount(user);
    }
}
