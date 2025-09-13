package com.spingo.controller;

import com.spingo.entity.Booking;
import com.spingo.entity.User;
import com.spingo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String customerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customer = (User) authentication.getPrincipal();
        
        // Get customer's bookings
        List<Booking> customerBookings = bookingService.getUserBookings(customer);
        
        // Calculate statistics
        long totalBookings = customerBookings.size();
        long activeBookings = customerBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
            .count();
        long completedBookings = customerBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
            .count();
        
        model.addAttribute("customer", customer);
        model.addAttribute("customerBookings", customerBookings);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("activeBookings", activeBookings);
        model.addAttribute("completedBookings", completedBookings);
        
        return "customer/dashboard";
    }
}
