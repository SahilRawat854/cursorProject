package com.spingo.repository;

import com.spingo.entity.Payment;
import com.spingo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    Payment findByBooking(Booking booking);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCESS' AND p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    List<Payment> findSuccessfulPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS'")
    Double getTotalRevenue();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.paymentDate >= :startDate AND p.paymentDate <= :endDate")
    Double getRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
}
