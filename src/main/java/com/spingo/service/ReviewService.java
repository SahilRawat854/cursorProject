package com.spingo.service;

import com.spingo.entity.Review;
import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.entity.Booking;
import com.spingo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BikeService bikeService;
    
    public Review createReview(User user, Bike bike, Booking booking, Integer rating, String reviewText) {
        // Check if user has already reviewed this bike
        Optional<Review> existingReview = reviewRepository.findByUserAndBike(user, bike);
        if (existingReview.isPresent()) {
            throw new RuntimeException("You have already reviewed this bike");
        }
        
        // Check if booking is completed
        if (!booking.getStatus().equals(Booking.BookingStatus.COMPLETED)) {
            throw new RuntimeException("You can only review bikes after completing the booking");
        }
        
        Review review = new Review(user, bike, booking, rating, reviewText);
        Review savedReview = reviewRepository.save(review);
        
        // Update bike rating
        updateBikeRating(bike);
        
        return savedReview;
    }
    
    public List<Review> getReviewsByBike(Bike bike) {
        return reviewRepository.findByBikeOrderByCreatedAtDesc(bike);
    }
    
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Double getAverageRating(Bike bike) {
        Double averageRating = reviewRepository.findAverageRatingByBike(bike);
        return averageRating != null ? averageRating : 0.0;
    }
    
    public Long getReviewCount(Bike bike) {
        return reviewRepository.countReviewsByBike(bike);
    }
    
    public List<Review> getReviewsByRating(Bike bike, Integer minRating) {
        return reviewRepository.findByBikeAndRatingGreaterThanEqual(bike, minRating);
    }
    
    private void updateBikeRating(Bike bike) {
        Double averageRating = getAverageRating(bike);
        Long reviewCount = getReviewCount(bike);
        
        bike.setAverageRating(averageRating);
        bike.setTotalReviews(reviewCount.intValue());
        bikeService.updateBike(bike);
    }
    
    public Review updateReview(Long reviewId, Integer rating, String reviewText) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        
        review.setRating(rating);
        review.setReviewText(reviewText);
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update bike rating
        updateBikeRating(review.getBike());
        
        return updatedReview;
    }
    
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));
        
        Bike bike = review.getBike();
        reviewRepository.delete(review);
        
        // Update bike rating
        updateBikeRating(bike);
    }
}
