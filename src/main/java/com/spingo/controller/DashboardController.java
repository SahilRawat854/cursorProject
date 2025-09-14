package com.spingo.controller;

import com.spingo.entity.User;
import com.spingo.entity.Bike;
import com.spingo.entity.Booking;
import com.spingo.entity.Notification;
import com.spingo.service.BikeService;
import com.spingo.service.BookingService;
import com.spingo.service.NotificationService;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BikeService bikeService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        
        // Get user-specific data
        model.addAttribute("user", user);
        
        // Get notifications
        List<Notification> notifications = notificationService.getUnreadNotifications(user);
        Long unreadCount = notificationService.getUnreadNotificationCount(user);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        
        // Redirect based on user type
        switch (user.getAccountType()) {
            case INDIVIDUAL_OWNER:
            case BUSINESS_OWNER:
                return ownerDashboard(user, model);
            case DELIVERY_PARTNER:
                return deliveryDashboard(user, model);
            case CUSTOMER:
            default:
                return customerDashboard(user, model);
        }
    }
    
    private String ownerDashboard(User user, Model model) {
        // Get owner's bikes
        List<Bike> ownedBikes = bikeService.getBikesByOwner(user);
        model.addAttribute("ownedBikes", ownedBikes);
        
        // Get recent bookings for owner's bikes
        List<Booking> recentBookings = bookingService.getRecentBookingsForOwner(user);
        model.addAttribute("recentBookings", recentBookings);
        
        // Get statistics
        long totalBikes = bikeService.getBikeCountByOwner(user);
        long availableBikes = ownedBikes.stream()
            .filter(bike -> bike.getStatus() == Bike.BikeStatus.AVAILABLE)
            .count();
        long bookedBikes = ownedBikes.stream()
            .filter(bike -> bike.getStatus() == Bike.BikeStatus.BOOKED)
            .count();
        
        model.addAttribute("totalBikes", totalBikes);
        model.addAttribute("availableBikes", availableBikes);
        model.addAttribute("bookedBikes", bookedBikes);
        
        return "dashboard/owner-dashboard";
    }
    
    private String deliveryDashboard(User user, Model model) {
        // Get delivery partner's bookings
        List<Booking> deliveryBookings = bookingService.getBookingsByUser(user);
        model.addAttribute("deliveryBookings", deliveryBookings);
        
        // Get available bikes for delivery
        List<Bike> availableBikes = bikeService.getAvailableBikes();
        model.addAttribute("availableBikes", availableBikes);
        
        // Get statistics
        long totalDeliveries = deliveryBookings.size();
        long completedDeliveries = deliveryBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
            .count();
        long activeDeliveries = deliveryBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
            .count();
        
        model.addAttribute("totalDeliveries", totalDeliveries);
        model.addAttribute("completedDeliveries", completedDeliveries);
        model.addAttribute("activeDeliveries", activeDeliveries);
        
        return "dashboard/delivery-dashboard";
    }
    
    private String customerDashboard(User user, Model model) {
        // Get customer's bookings
        List<Booking> customerBookings = bookingService.getBookingsByUser(user);
        model.addAttribute("customerBookings", customerBookings);
        
        // Get recommended bikes
        List<Bike> recommendedBikes = bikeService.getRecommendedBikes();
        model.addAttribute("recommendedBikes", recommendedBikes);
        
        // Get popular bikes
        List<Bike> popularBikes = bikeService.getPopularBikes();
        model.addAttribute("popularBikes", popularBikes);
        
        // Get statistics
        long totalBookings = customerBookings.size();
        long completedBookings = customerBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
            .count();
        long activeBookings = customerBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
            .count();
        
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("completedBookings", completedBookings);
        model.addAttribute("activeBookings", activeBookings);
        
        return "dashboard/customer-dashboard";
    }
    
    @GetMapping("/analytics")
    public String analytics(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        
        if (user.getAccountType() == User.AccountType.CUSTOMER) {
            return "redirect:/dashboard";
        }
        
        // Get analytics data for owners
        List<Bike> ownedBikes = bikeService.getBikesByOwner(user);
        List<Booking> allBookings = bookingService.getRecentBookingsForOwner(user);
        
        model.addAttribute("user", user);
        model.addAttribute("ownedBikes", ownedBikes);
        model.addAttribute("allBookings", allBookings);
        
        return "dashboard/analytics";
    }
}
