package com.spingo.repository;

import com.spingo.entity.Booking;
import com.spingo.entity.Bike;
import com.spingo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUser(User user);
    List<Booking> findByBike(Bike bike);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByPaymentStatus(Booking.PaymentStatus paymentStatus);
    List<Booking> findByBookingType(Booking.BookingType bookingType);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user ORDER BY b.createdAt DESC")
    List<Booking> findUserBookingsOrderByDate(@Param("user") User user);
    
    @Query("SELECT b FROM Booking b WHERE b.bike = :bike AND b.status IN ('CONFIRMED', 'ACTIVE')")
    List<Booking> findActiveBookingsForBike(@Param("bike") Bike bike);
    
    @Query("SELECT b FROM Booking b WHERE b.bike = :bike AND " +
           "((b.startTime <= :endTime AND b.endTime >= :startTime) OR " +
           "(b.startTime <= :startTime AND b.endTime >= :endTime))")
    List<Booking> findConflictingBookings(@Param("bike") Bike bike, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'COMPLETED'")
    long countCompletedBookings();
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user = :user AND b.status = 'COMPLETED'")
    long countCompletedBookingsByUser(@Param("user") User user);
    
    @Query("SELECT b FROM Booking b WHERE b.bike.owner = :owner ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookingsForOwner(@Param("owner") User owner);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.startTime > :now ORDER BY b.startTime ASC")
    List<Booking> findUpcomingBookings(@Param("user") User user, @Param("now") LocalDateTime now);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.status = 'ACTIVE'")
    List<Booking> findActiveBookingsByUser(@Param("user") User user);
    
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.status IN ('CONFIRMED', 'ACTIVE') ORDER BY b.startTime ASC")
    List<Booking> findCurrentBookingsByUser(@Param("user") User user);
}
