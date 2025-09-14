package com.spingo.service;

import com.spingo.entity.Bike;
import com.spingo.entity.Booking;
import com.spingo.entity.User;
import com.spingo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        // Check for conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getBike(), booking.getStartTime(), booking.getEndTime());
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Bike is not available for the selected time period");
        }
        
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findUserBookingsOrderByDate(user);
    }

    public List<Booking> getBikeBookings(Bike bike) {
        return bookingRepository.findByBike(bike);
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> getActiveBookingsForBike(Bike bike) {
        return bookingRepository.findActiveBookingsForBike(bike);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() == Booking.BookingStatus.ACTIVE) {
            throw new RuntimeException("Cannot cancel an active booking");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public void extendBooking(Long id, LocalDateTime newEndTime) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() != Booking.BookingStatus.ACTIVE) {
            throw new RuntimeException("Can only extend active bookings");
        }
        
        if (newEndTime.isBefore(booking.getEndTime())) {
            throw new RuntimeException("New end time must be after current end time");
        }
        
        // Check for conflicts with the new end time
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getBike(), booking.getStartTime(), newEndTime);
        
        // Remove current booking from conflicts
        conflicts.removeIf(b -> b.getId().equals(booking.getId()));
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Bike is not available for the extended time period");
        }
        
        booking.setEndTime(newEndTime);
        booking.setStatus(Booking.BookingStatus.EXTENDED);
        bookingRepository.save(booking);
    }

    public void confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    public void startBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Can only start confirmed bookings");
        }
        
        booking.setStatus(Booking.BookingStatus.ACTIVE);
        bookingRepository.save(booking);
    }

    public void completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() != Booking.BookingStatus.ACTIVE) {
            throw new RuntimeException("Can only complete active bookings");
        }
        
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        bookingRepository.save(booking);
    }

    public long getCompletedBookingsCount() {
        return bookingRepository.countCompletedBookings();
    }

    public long getUserCompletedBookingsCount(User user) {
        return bookingRepository.countCompletedBookingsByUser(user);
    }
    
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
    
    public List<Booking> getRecentBookingsForOwner(User owner) {
        return bookingRepository.findRecentBookingsForOwner(owner);
    }
    
    public List<Booking> getUpcomingBookings(User user) {
        return bookingRepository.findUpcomingBookings(user, LocalDateTime.now());
    }
    
    public List<Booking> getActiveBookings(User user) {
        return bookingRepository.findActiveBookingsByUser(user);
    }
}
