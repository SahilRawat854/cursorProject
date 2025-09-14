package com.spingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bikes")
public class Bike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Bike name is required")
    private String name;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bike type is required")
    private BikeType type;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;
    
    @NotNull(message = "Engine capacity is required")
    private Integer engineCapacity;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotBlank(message = "Registration number is required")
    @Column(unique = true, nullable = false)
    private String registrationNumber;
    
    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be positive")
    private BigDecimal dailyRate;
    
    @NotNull(message = "Monthly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly rate must be positive")
    private BigDecimal monthlyRate;
    
    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be positive")
    private BigDecimal hourlyRate;
    
    @Enumerated(EnumType.STRING)
    private BikeStatus status = BikeStatus.AVAILABLE;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String description;
    private String imageUrl;
    private Boolean hasHelmet = false;
    private Boolean hasNavigation = false;
    private Boolean isInsured = false;
    private Integer mileage;
    @Column(name = "`condition`")
    private String condition;
    private Double averageRating = 0.0;
    private Integer totalReviews = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Bike() {}
    
    public Bike(String name, String brand, String model, Integer year, BikeType type, 
                FuelType fuelType, Integer engineCapacity, String color, String registrationNumber,
                BigDecimal dailyRate, BigDecimal monthlyRate, BigDecimal hourlyRate, 
                String location, User owner) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;
        this.fuelType = fuelType;
        this.engineCapacity = engineCapacity;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.dailyRate = dailyRate;
        this.monthlyRate = monthlyRate;
        this.hourlyRate = hourlyRate;
        this.location = location;
        this.owner = owner;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public BikeType getType() { return type; }
    public void setType(BikeType type) { this.type = type; }
    
    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    
    public Integer getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(Integer engineCapacity) { this.engineCapacity = engineCapacity; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    
    public BigDecimal getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(BigDecimal monthlyRate) { this.monthlyRate = monthlyRate; }
    
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public BikeStatus getStatus() { return status; }
    public void setStatus(BikeStatus status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getHasHelmet() { return hasHelmet; }
    public void setHasHelmet(Boolean hasHelmet) { this.hasHelmet = hasHelmet; }
    
    public Boolean getHasNavigation() { return hasNavigation; }
    public void setHasNavigation(Boolean hasNavigation) { this.hasNavigation = hasNavigation; }
    
    public Boolean getIsInsured() { return isInsured; }
    public void setIsInsured(Boolean isInsured) { this.isInsured = isInsured; }
    
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    
    // Enums
    public enum BikeType {
        SCOOTER, MOTORCYCLE, ELECTRIC_BIKE, SPORTS_BIKE, CRUISER
    }
    
    public enum FuelType {
        PETROL, ELECTRIC, HYBRID
    }
    
    public enum BikeStatus {
        AVAILABLE, BOOKED, MAINTENANCE, OUT_OF_SERVICE
    }
}
