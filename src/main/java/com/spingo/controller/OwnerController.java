package com.spingo.controller;

import com.spingo.entity.Bike;
import com.spingo.entity.Booking;
import com.spingo.entity.User;
import com.spingo.service.BikeService;
import com.spingo.service.BookingService;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String ownerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User owner = (User) authentication.getPrincipal();
        
        // Get owner's bikes
        List<Bike> ownedBikes = bikeService.getBikesByOwner(owner);
        
        // Get bookings for owner's bikes
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Booking> ownerBookings = allBookings.stream()
            .filter(booking -> ownedBikes.contains(booking.getBike()))
            .toList();
        
        // Calculate statistics
        long totalBikes = ownedBikes.size();
        long availableBikes = ownedBikes.stream()
            .filter(bike -> bike.getStatus() == Bike.BikeStatus.AVAILABLE)
            .count();
        long totalBookings = ownerBookings.size();
        long activeBookings = ownerBookings.stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.ACTIVE)
            .count();
        
        model.addAttribute("owner", owner);
        model.addAttribute("ownedBikes", ownedBikes);
        model.addAttribute("ownerBookings", ownerBookings);
        model.addAttribute("totalBikes", totalBikes);
        model.addAttribute("availableBikes", availableBikes);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("activeBookings", activeBookings);
        
        return "owner/dashboard";
    }
}
