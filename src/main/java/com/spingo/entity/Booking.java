package com.spingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;
    
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Booking type is required")
    private BookingType bookingType;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be positive")
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private String pickupLocation;
    private String dropoffLocation;
    private String notes;
    private Boolean helmetRequested = false;
    private Boolean navigationRequested = false;
    private BigDecimal helmetFee = BigDecimal.ZERO;
    private BigDecimal navigationFee = BigDecimal.ZERO;
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Booking() {}
    
    public Booking(User user, Bike bike, LocalDateTime startTime, LocalDateTime endTime, 
                   BookingType bookingType, BigDecimal totalAmount, String pickupLocation) {
        this.user = user;
        this.bike = bike;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingType = bookingType;
        this.totalAmount = totalAmount;
        this.pickupLocation = pickupLocation;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Bike getBike() { return bike; }
    public void setBike(Bike bike) { this.bike = bike; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public BookingType getBookingType() { return bookingType; }
    public void setBookingType(BookingType bookingType) { this.bookingType = bookingType; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    
    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Boolean getHelmetRequested() { return helmetRequested; }
    public void setHelmetRequested(Boolean helmetRequested) { this.helmetRequested = helmetRequested; }
    
    public Boolean getNavigationRequested() { return navigationRequested; }
    public void setNavigationRequested(Boolean navigationRequested) { this.navigationRequested = navigationRequested; }
    
    public BigDecimal getHelmetFee() { return helmetFee; }
    public void setHelmetFee(BigDecimal helmetFee) { this.helmetFee = helmetFee; }
    
    public BigDecimal getNavigationFee() { return navigationFee; }
    public void setNavigationFee(BigDecimal navigationFee) { this.navigationFee = navigationFee; }
    
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Enums
    public enum BookingType {
        HOURLY, DAILY, MONTHLY, SUBSCRIPTION
    }
    
    public enum BookingStatus {
        PENDING, CONFIRMED, ACTIVE, COMPLETED, CANCELLED, EXTENDED
    }
    
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}
