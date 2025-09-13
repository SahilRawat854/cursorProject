package com.spingo.controller;

import com.spingo.entity.Bike;
import com.spingo.entity.Booking;
import com.spingo.entity.User;
import com.spingo.service.BikeService;
import com.spingo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public String listBookings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        List<Booking> bookings = bookingService.getUserBookings(user);
        model.addAttribute("bookings", bookings);
        
        return "bookings/list";
    }

    @GetMapping("/new/{bikeId}")
    public String newBookingForm(@PathVariable Long bikeId, Model model) {
        Bike bike = bikeService.findById(bikeId)
            .orElseThrow(() -> new RuntimeException("Bike not found"));
        
        model.addAttribute("bike", bike);
        model.addAttribute("booking", new Booking());
        model.addAttribute("bookingTypes", Booking.BookingType.values());
        
        return "bookings/new";
    }

    @PostMapping("/new")
    public String createBooking(@ModelAttribute Booking booking, 
                               @RequestParam Long bikeId,
                               @RequestParam(required = false) Boolean helmetRequested,
                               @RequestParam(required = false) Boolean navigationRequested,
                               RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            
            Bike bike = bikeService.findById(bikeId)
                .orElseThrow(() -> new RuntimeException("Bike not found"));
            
            // Calculate total amount
            BigDecimal totalAmount = calculateBookingAmount(booking, bike, helmetRequested, navigationRequested);
            
            // Set booking details
            booking.setUser(user);
            booking.setBike(bike);
            booking.setTotalAmount(totalAmount);
            booking.setHelmetRequested(helmetRequested != null ? helmetRequested : false);
            booking.setNavigationRequested(navigationRequested != null ? navigationRequested : false);
            booking.setStatus(Booking.BookingStatus.PENDING);
            booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
            
            // Add helmet and navigation fees
            if (helmetRequested != null && helmetRequested) {
                booking.setHelmetFee(new BigDecimal("50"));
            }
            if (navigationRequested != null && navigationRequested) {
                booking.setNavigationFee(new BigDecimal("100"));
            }
            
            Booking savedBooking = bookingService.createBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
            return "redirect:/bookings/" + savedBooking.getId() + "/payment";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating booking: " + e.getMessage());
            return "redirect:/bikes/" + bikeId;
        }
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        model.addAttribute("booking", booking);
        return "bookings/details";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/bookings";
    }

    @PostMapping("/{id}/extend")
    public String extendBooking(@PathVariable Long id, 
                               @RequestParam LocalDateTime newEndTime,
                               RedirectAttributes redirectAttributes) {
        try {
            bookingService.extendBooking(id, newEndTime);
            redirectAttributes.addFlashAttribute("success", "Booking extended successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error extending booking: " + e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }

    private BigDecimal calculateBookingAmount(Booking booking, Bike bike, Boolean helmetRequested, Boolean navigationRequested) {
        BigDecimal baseAmount = BigDecimal.ZERO;
        
        // Calculate base amount based on booking type
        switch (booking.getBookingType()) {
            case HOURLY:
                long hours = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
                baseAmount = bike.getHourlyRate().multiply(BigDecimal.valueOf(hours));
                break;
            case DAILY:
                long days = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toDays();
                baseAmount = bike.getDailyRate().multiply(BigDecimal.valueOf(days));
                break;
            case MONTHLY:
                long months = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toDays() / 30;
                baseAmount = bike.getMonthlyRate().multiply(BigDecimal.valueOf(months));
                break;
            case SUBSCRIPTION:
                baseAmount = bike.getMonthlyRate();
                break;
        }
        
        // Add additional fees
        if (helmetRequested != null && helmetRequested) {
            baseAmount = baseAmount.add(new BigDecimal("50"));
        }
        if (navigationRequested != null && navigationRequested) {
            baseAmount = baseAmount.add(new BigDecimal("100"));
        }
        
        return baseAmount;
    }
}
