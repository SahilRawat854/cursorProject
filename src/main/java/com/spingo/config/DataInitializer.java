package com.spingo.config;

import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.service.BikeService;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if no users exist
        if (userService.getAllUsers().isEmpty()) {
            initializeUsers();
            initializeBikes();
        }
    }

    private void initializeUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@spingo.com");
        admin.setPhone("9999999999");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("System Administrator");
        admin.setAge(30);
        admin.setAddress("Admin Office, Mumbai, Maharashtra");
        admin.setDrivingLicense("ADMIN001");
        admin.setAccountType(User.AccountType.INDIVIDUAL_OWNER);
        userService.registerUser(admin);

        // Create sample bike owner
        User owner1 = new User();
        owner1.setUsername("john_owner");
        owner1.setEmail("john@example.com");
        owner1.setPhone("9876543210");
        owner1.setPassword(passwordEncoder.encode("password123"));
        owner1.setFullName("John Smith");
        owner1.setAge(28);
        owner1.setAddress("123 Main Street, Bangalore, Karnataka");
        owner1.setDrivingLicense("DL001234567");
        owner1.setAccountType(User.AccountType.INDIVIDUAL_OWNER);
        userService.registerUser(owner1);

        // Create sample customer
        User customer1 = new User();
        customer1.setUsername("jane_customer");
        customer1.setEmail("jane@example.com");
        customer1.setPhone("9876543211");
        customer1.setPassword(passwordEncoder.encode("password123"));
        customer1.setFullName("Jane Doe");
        customer1.setAge(25);
        customer1.setAddress("456 Park Avenue, Delhi, NCR");
        customer1.setDrivingLicense("DL001234568");
        customer1.setAccountType(User.AccountType.CUSTOMER);
        userService.registerUser(customer1);

        // Create business owner
        User businessOwner = new User();
        businessOwner.setUsername("bike_rental_co");
        businessOwner.setEmail("info@bikerental.com");
        businessOwner.setPhone("9876543212");
        businessOwner.setPassword(passwordEncoder.encode("password123"));
        businessOwner.setFullName("Bike Rental Company");
        businessOwner.setAge(35);
        businessOwner.setAddress("789 Business Park, Pune, Maharashtra");
        businessOwner.setDrivingLicense("DL001234569");
        businessOwner.setAccountType(User.AccountType.BUSINESS_OWNER);
        userService.registerUser(businessOwner);

        // Create delivery partner
        User deliveryPartner = new User();
        deliveryPartner.setUsername("delivery_partner");
        deliveryPartner.setEmail("delivery@example.com");
        deliveryPartner.setPhone("9876543213");
        deliveryPartner.setPassword(passwordEncoder.encode("password123"));
        deliveryPartner.setFullName("Mike Johnson");
        deliveryPartner.setAge(32);
        deliveryPartner.setAddress("321 Delivery Hub, Chennai, Tamil Nadu");
        deliveryPartner.setDrivingLicense("DL001234570");
        deliveryPartner.setAccountType(User.AccountType.DELIVERY_PARTNER);
        userService.registerUser(deliveryPartner);
    }

    private void initializeBikes() {
        // Get the bike owner
        User owner = userService.findByUsername("john_owner").orElse(null);
        User businessOwner = userService.findByUsername("bike_rental_co").orElse(null);

        if (owner != null) {
            // Create sample bikes for individual owner
            Bike bike1 = new Bike();
            bike1.setName("Honda Activa 6G");
            bike1.setBrand("Honda");
            bike1.setModel("Activa 6G");
            bike1.setYear(2023);
            bike1.setType(Bike.BikeType.SCOOTER);
            bike1.setFuelType(Bike.FuelType.PETROL);
            bike1.setEngineCapacity(110);
            bike1.setColor("Red");
            bike1.setRegistrationNumber("KA01AB1234");
            bike1.setDailyRate(new BigDecimal("500"));
            bike1.setMonthlyRate(new BigDecimal("5000"));
            bike1.setHourlyRate(new BigDecimal("50"));
            bike1.setLocation("Bangalore, Karnataka");
            bike1.setDescription("Well-maintained Honda Activa with low mileage. Perfect for city rides.");
            bike1.setHasHelmet(true);
            bike1.setHasNavigation(false);
            bike1.setIsInsured(true);
            bike1.setOwner(owner);
            bikeService.addBike(bike1);

            Bike bike2 = new Bike();
            bike2.setName("Bajaj Pulsar 150");
            bike2.setBrand("Bajaj");
            bike2.setModel("Pulsar 150");
            bike2.setYear(2022);
            bike2.setType(Bike.BikeType.MOTORCYCLE);
            bike2.setFuelType(Bike.FuelType.PETROL);
            bike2.setEngineCapacity(150);
            bike2.setColor("Black");
            bike2.setRegistrationNumber("KA02CD5678");
            bike2.setDailyRate(new BigDecimal("600"));
            bike2.setMonthlyRate(new BigDecimal("6000"));
            bike2.setHourlyRate(new BigDecimal("60"));
            bike2.setLocation("Bangalore, Karnataka");
            bike2.setDescription("Sporty Bajaj Pulsar with excellent performance. Great for highway rides.");
            bike2.setHasHelmet(true);
            bike2.setHasNavigation(true);
            bike2.setIsInsured(true);
            bike2.setOwner(owner);
            bikeService.addBike(bike2);
        }

        if (businessOwner != null) {
            // Create sample bikes for business owner
            Bike bike3 = new Bike();
            bike3.setName("TVS Jupiter");
            bike3.setBrand("TVS");
            bike3.setModel("Jupiter");
            bike3.setYear(2023);
            bike3.setType(Bike.BikeType.SCOOTER);
            bike3.setFuelType(Bike.FuelType.PETROL);
            bike3.setEngineCapacity(110);
            bike3.setColor("Blue");
            bike3.setRegistrationNumber("MH12EF9012");
            bike3.setDailyRate(new BigDecimal("450"));
            bike3.setMonthlyRate(new BigDecimal("4500"));
            bike3.setHourlyRate(new BigDecimal("45"));
            bike3.setLocation("Pune, Maharashtra");
            bike3.setDescription("Comfortable TVS Jupiter with good fuel efficiency. Ideal for daily commuting.");
            bike3.setHasHelmet(true);
            bike3.setHasNavigation(false);
            bike3.setIsInsured(true);
            bike3.setOwner(businessOwner);
            bikeService.addBike(bike3);

            Bike bike4 = new Bike();
            bike4.setName("Yamaha R15 V4");
            bike4.setBrand("Yamaha");
            bike4.setModel("R15 V4");
            bike4.setYear(2023);
            bike4.setType(Bike.BikeType.SPORTS_BIKE);
            bike4.setFuelType(Bike.FuelType.PETROL);
            bike4.setEngineCapacity(155);
            bike4.setColor("Blue");
            bike4.setRegistrationNumber("MH12GH3456");
            bike4.setDailyRate(new BigDecimal("800"));
            bike4.setMonthlyRate(new BigDecimal("8000"));
            bike4.setHourlyRate(new BigDecimal("80"));
            bike4.setLocation("Pune, Maharashtra");
            bike4.setDescription("High-performance Yamaha R15 V4. Perfect for sports bike enthusiasts.");
            bike4.setHasHelmet(true);
            bike4.setHasNavigation(true);
            bike4.setIsInsured(true);
            bike4.setOwner(businessOwner);
            bikeService.addBike(bike4);

            Bike bike5 = new Bike();
            bike5.setName("Hero Electric Optima");
            bike5.setBrand("Hero Electric");
            bike5.setModel("Optima");
            bike5.setYear(2023);
            bike5.setType(Bike.BikeType.ELECTRIC_BIKE);
            bike5.setFuelType(Bike.FuelType.ELECTRIC);
            bike5.setEngineCapacity(0);
            bike5.setColor("White");
            bike5.setRegistrationNumber("MH12IJ7890");
            bike5.setDailyRate(new BigDecimal("400"));
            bike5.setMonthlyRate(new BigDecimal("4000"));
            bike5.setHourlyRate(new BigDecimal("40"));
            bike5.setLocation("Pune, Maharashtra");
            bike5.setDescription("Eco-friendly Hero Electric Optima. Zero emissions, perfect for city rides.");
            bike5.setHasHelmet(true);
            bike5.setHasNavigation(false);
            bike5.setIsInsured(true);
            bike5.setOwner(businessOwner);
            bikeService.addBike(bike5);
        }
    }
}
