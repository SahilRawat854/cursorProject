package com.spingo.service;

import com.spingo.entity.Booking;
import com.spingo.entity.Payment;
import com.spingo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Booking booking, Payment.PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        
        return paymentRepository.save(payment);
    }

    public Payment processPayment(String transactionId, String paymentGatewayResponse) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Simulate payment processing with dummy API
        boolean paymentSuccess = simulatePaymentProcessing(payment, paymentGatewayResponse);
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentGatewayResponse(paymentGatewayResponse);
            
            // Update booking payment status
            Booking booking = payment.getBooking();
            booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Payment processing failed");
        }
        
        return paymentRepository.save(payment);
    }

    public Payment refundPayment(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Can only refund successful payments");
        }
        
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setFailureReason(reason);
        
        // Update booking payment status
        Booking booking = payment.getBooking();
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);
        
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Optional<Payment> findByBooking(Booking booking) {
        return Optional.ofNullable(paymentRepository.findByBooking(booking));
    }

    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getSuccessfulPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findSuccessfulPaymentsBetweenDates(startDate, endDate);
    }

    public Double getTotalRevenue() {
        Double revenue = paymentRepository.getTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public Double getRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = paymentRepository.getRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    private String generateTransactionId() {
        return "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private boolean simulatePaymentProcessing(Payment payment, String paymentGatewayResponse) {
        // Simulate payment processing with 90% success rate
        // In real implementation, this would call actual payment gateway APIs
        
        try {
            // Simulate API call delay
            Thread.sleep(2000);
            
            // Simulate different payment method success rates
            switch (payment.getPaymentMethod()) {
                case UPI:
                    return Math.random() > 0.05; // 95% success rate
                case CREDIT_CARD:
                case DEBIT_CARD:
                    return Math.random() > 0.08; // 92% success rate
                case NET_BANKING:
                    return Math.random() > 0.10; // 90% success rate
                case WALLET:
                    return Math.random() > 0.12; // 88% success rate
                case CASH:
                    return true; // 100% success rate for cash
                default:
                    return Math.random() > 0.10; // 90% success rate
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
