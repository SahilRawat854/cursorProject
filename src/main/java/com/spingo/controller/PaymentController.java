package com.spingo.controller;

import com.spingo.entity.Booking;
import com.spingo.entity.Payment;
import com.spingo.service.BookingService;
import com.spingo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/{bookingId}")
    public String paymentPage(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Check if user owns this booking
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.spingo.entity.User) {
            com.spingo.entity.User user = (com.spingo.entity.User) principal;
            if (!booking.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied");
            }
        }
        
        // Get or create payment
        Optional<Payment> existingPayment = paymentService.findByBooking(booking);
        Payment payment = existingPayment.orElse(null);
        
        model.addAttribute("booking", booking);
        model.addAttribute("payment", payment);
        model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
        
        return "payments/payment";
    }

    @PostMapping("/process")
    public String processPayment(@RequestParam Long bookingId,
                                @RequestParam Payment.PaymentMethod paymentMethod,
                                @RequestParam(required = false) String paymentDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
            
            // Create or get existing payment
            Optional<Payment> existingPayment = paymentService.findByBooking(booking);
            Payment payment;
            
            if (existingPayment.isPresent()) {
                payment = existingPayment.get();
                payment.setPaymentMethod(paymentMethod);
            } else {
                payment = paymentService.createPayment(booking, paymentMethod);
            }
            
            // Process payment
            String paymentGatewayResponse = generatePaymentGatewayResponse(paymentMethod, paymentDetails);
            Payment processedPayment = paymentService.processPayment(payment.getTransactionId(), paymentGatewayResponse);
            
            if (processedPayment.getStatus() == Payment.PaymentStatus.SUCCESS) {
                // Confirm booking
                bookingService.confirmBooking(bookingId);
                redirectAttributes.addFlashAttribute("success", "Payment successful! Your booking has been confirmed.");
                return "redirect:/bookings/" + bookingId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment failed. Please try again.");
                return "redirect:/payments/" + bookingId;
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment processing failed: " + e.getMessage());
            return "redirect:/payments/" + bookingId;
        }
    }

    @PostMapping("/refund/{paymentId}")
    public String refundPayment(@PathVariable Long paymentId,
                               @RequestParam String reason,
                               RedirectAttributes redirectAttributes) {
        try {
            Payment refundedPayment = paymentService.refundPayment(paymentId, reason);
            redirectAttributes.addFlashAttribute("success", "Refund processed successfully.");
            return "redirect:/bookings/" + refundedPayment.getBooking().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Refund failed: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @GetMapping("/success/{transactionId}")
    public String paymentSuccess(@PathVariable String transactionId, Model model) {
        Payment payment = paymentService.findByTransactionId(transactionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        model.addAttribute("payment", payment);
        model.addAttribute("booking", payment.getBooking());
        
        return "payments/success";
    }

    @GetMapping("/failure/{transactionId}")
    public String paymentFailure(@PathVariable String transactionId, Model model) {
        Payment payment = paymentService.findByTransactionId(transactionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        model.addAttribute("payment", payment);
        model.addAttribute("booking", payment.getBooking());
        
        return "payments/failure";
    }

    private String generatePaymentGatewayResponse(Payment.PaymentMethod paymentMethod, String paymentDetails) {
        // Simulate payment gateway response
        return String.format("PAYMENT_%s_%d_%s", 
            paymentMethod.name(), 
            System.currentTimeMillis(), 
            paymentDetails != null ? paymentDetails : "DEFAULT");
    }
}
