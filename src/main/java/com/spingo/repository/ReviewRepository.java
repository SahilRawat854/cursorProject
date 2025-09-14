package com.spingo.repository;

import com.spingo.entity.Review;
import com.spingo.entity.Bike;
import com.spingo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByBikeOrderByCreatedAtDesc(Bike bike);
    
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    
    Optional<Review> findByUserAndBike(User user, Bike bike);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.bike = :bike")
    Double findAverageRatingByBike(@Param("bike") Bike bike);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.bike = :bike")
    Long countReviewsByBike(@Param("bike") Bike bike);
    
    @Query("SELECT r FROM Review r WHERE r.bike = :bike AND r.rating >= :minRating ORDER BY r.createdAt DESC")
    List<Review> findByBikeAndRatingGreaterThanEqual(@Param("bike") Bike bike, @Param("minRating") Integer minRating);
}
