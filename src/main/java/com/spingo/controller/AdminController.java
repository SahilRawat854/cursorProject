package com.spingo.controller;

import com.spingo.entity.Bike;
import com.spingo.entity.Booking;
import com.spingo.entity.Payment;
import com.spingo.entity.User;
import com.spingo.service.BikeService;
import com.spingo.service.BookingService;
import com.spingo.service.PaymentService;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Get statistics
        long totalUsers = userService.getActiveUserCount();
        long totalBikes = bikeService.getAvailableBikeCount();
        long totalBookings = bookingService.getCompletedBookingsCount();
        double totalRevenue = paymentService.getTotalRevenue();
        
        // Get recent activities
        List<User> recentUsers = userService.getAllUsers().stream().limit(5).toList();
        List<Bike> recentBikes = bikeService.getAllBikes().stream().limit(5).toList();
        List<Booking> recentBookings = bookingService.getAllBookings().stream().limit(5).toList();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBikes", totalBikes);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("recentUsers", recentUsers);
        model.addAttribute("recentBikes", recentBikes);
        model.addAttribute("recentBookings", recentBookings);
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (user.getStatus() == User.UserStatus.ACTIVE) {
                user.setStatus(User.UserStatus.LOCKED);
            } else {
                user.setStatus(User.UserStatus.ACTIVE);
            }
            
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/bikes")
    public String manageBikes(Model model) {
        List<Bike> bikes = bikeService.getAllBikes();
        model.addAttribute("bikes", bikes);
        return "admin/bikes";
    }

    @GetMapping("/bikes/{id}/toggle-status")
    public String toggleBikeStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Bike bike = bikeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found"));
            
            if (bike.getStatus() == Bike.BikeStatus.AVAILABLE) {
                bike.setStatus(Bike.BikeStatus.OUT_OF_SERVICE);
            } else {
                bike.setStatus(Bike.BikeStatus.AVAILABLE);
            }
            
            bikeService.updateBike(bike);
            redirectAttributes.addFlashAttribute("success", "Bike status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating bike status: " + e.getMessage());
        }
        return "redirect:/admin/bikes";
    }

    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }

    @GetMapping("/bookings/{id}/confirm")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking confirmed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error confirming booking: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @GetMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @GetMapping("/payments")
    public String managePayments(Model model) {
        List<Payment> payments = paymentService.getPaymentsByStatus(Payment.PaymentStatus.SUCCESS);
        model.addAttribute("payments", payments);
        return "admin/payments";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        // Get analytics data
        long totalUsers = userService.getActiveUserCount();
        long totalBikes = bikeService.getAvailableBikeCount();
        long totalBookings = bookingService.getCompletedBookingsCount();
        double totalRevenue = paymentService.getTotalRevenue();
        
        // Get revenue for last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        double monthlyRevenue = paymentService.getRevenueBetweenDates(thirtyDaysAgo, LocalDateTime.now());
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBikes", totalBikes);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        
        return "admin/analytics";
    }
}
