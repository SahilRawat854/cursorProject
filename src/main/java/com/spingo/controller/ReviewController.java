package com.spingo.controller;

import com.spingo.entity.Review;
import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.entity.Booking;
import com.spingo.service.ReviewService;
import com.spingo.service.BikeService;
import com.spingo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private BikeService bikeService;
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/bike/{bikeId}")
    public String getBikeReviews(@PathVariable Long bikeId, Model model) {
        Bike bike = bikeService.getBikeById(bikeId)
            .orElseThrow(() -> new RuntimeException("Bike not found"));
        
        List<Review> reviews = reviewService.getReviewsByBike(bike);
        Double averageRating = reviewService.getAverageRating(bike);
        Long reviewCount = reviewService.getReviewCount(bike);
        
        model.addAttribute("bike", bike);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);
        
        return "reviews/bike-reviews";
    }
    
    @GetMapping("/create/{bookingId}")
    public String createReviewForm(@PathVariable Long bookingId, Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Booking booking = bookingService.getBookingById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only review your own bookings");
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("review", new Review());
        
        return "reviews/create-review";
    }
    
    @PostMapping("/create")
    public String createReview(@RequestParam Long bookingId,
                             @RequestParam Integer rating,
                             @RequestParam String reviewText,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            Booking booking = bookingService.getBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
            
            reviewService.createReview(user, booking.getBike(), booking, rating, reviewText);
            
            redirectAttributes.addFlashAttribute("success", "Review submitted successfully!");
            return "redirect:/reviews/bike/" + booking.getBike().getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reviews/create/" + bookingId;
        }
    }
    
    @GetMapping("/my-reviews")
    public String getMyReviews(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Review> reviews = reviewService.getReviewsByUser(user);
        
        model.addAttribute("reviews", reviews);
        
        return "reviews/my-reviews";
    }
    
    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId, 
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = (User) authentication.getPrincipal();
            Review review = reviewService.getReviewsByUser(user).stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));
            
            reviewService.deleteReview(reviewId);
            
            redirectAttributes.addFlashAttribute("success", "Review deleted successfully!");
            return "redirect:/reviews/my-reviews";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reviews/my-reviews";
        }
    }
}
