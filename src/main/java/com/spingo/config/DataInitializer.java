package com.spingo.config;

import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.entity.Booking;
import com.spingo.entity.Payment;
import com.spingo.entity.Review;
import com.spingo.entity.Location;
import com.spingo.entity.Notification;
import com.spingo.service.BikeService;
import com.spingo.service.UserService;
import com.spingo.service.BookingService;
import com.spingo.service.PaymentService;
import com.spingo.service.ReviewService;
import com.spingo.service.LocationService;
import com.spingo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if no users exist
        if (userService.getAllUsers().isEmpty()) {
            initializeLocations();
            initializeUsers();
            initializeBikes();
            initializeBookings();
            initializePayments();
            initializeReviews();
            initializeNotifications();
        }
    }

    private void initializeLocations() {
        List<String> cities = Arrays.asList(
            "Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata", "Hyderabad", 
            "Pune", "Ahmedabad", "Jaipur", "Surat", "Lucknow", "Kanpur",
            "Nagpur", "Indore", "Thane", "Bhopal", "Visakhapatnam", "Pimpri-Chinchwad",
            "Patna", "Vadodara", "Ghaziabad", "Ludhiana", "Agra", "Nashik"
        );

        for (String city : cities) {
            Location location = new Location();
            location.setName(city + " Central");
            location.setCity(city);
            location.setAddress("Main Street, " + city);
            location.setLatitude(19.0760 + (random.nextDouble() - 0.5) * 0.1);
            location.setLongitude(72.8777 + (random.nextDouble() - 0.5) * 0.1);
            location.setAvailableSlots(random.nextInt(20) + 10);
            location.setTotalSlots(location.getAvailableSlots() + random.nextInt(10));
            location.setType(Location.LocationType.PICKUP_DROP);
            location.setStatus(Location.LocationStatus.ACTIVE);
            locationService.addLocation(location);
        }
    }

    private void initializeUsers() {
        // Create admin user
        User admin = createUser("admin", "admin@spingo.com", "9999999999", "admin123", 
                               "System Administrator", 30, "Admin Office, Mumbai, Maharashtra", 
                               "ADMIN001", User.AccountType.INDIVIDUAL_OWNER);
        userService.registerUser(admin);

        // Create individual bike owners
        String[] ownerNames = {"John Smith", "Sarah Johnson", "Mike Wilson", "Emily Davis", "David Brown", "Lisa Anderson"};
        String[] ownerCities = {"Bangalore", "Delhi", "Mumbai", "Chennai", "Pune", "Hyderabad"};
        
        for (int i = 0; i < ownerNames.length; i++) {
            User owner = createUser(
                "owner_" + (i + 1),
                "owner" + (i + 1) + "@example.com",
                "987654321" + i,
                "password123",
                ownerNames[i],
                25 + random.nextInt(15),
                "123 Main Street, " + ownerCities[i],
                "DL00123456" + i,
                User.AccountType.INDIVIDUAL_OWNER
            );
            userService.registerUser(owner);
        }

        // Create business owners
        String[] businessNames = {"Bike Rental Co", "City Bikes Ltd", "Urban Rides", "Metro Bikes", "Quick Rentals"};
        String[] businessCities = {"Mumbai", "Delhi", "Bangalore", "Chennai", "Pune"};
        
        for (int i = 0; i < businessNames.length; i++) {
            User businessOwner = createUser(
                "business_" + (i + 1),
                "business" + (i + 1) + "@company.com",
                "987654322" + i,
                "password123",
                businessNames[i],
                35 + random.nextInt(10),
                "Business Park, " + businessCities[i],
                "DL00123457" + i,
                User.AccountType.BUSINESS_OWNER
            );
            userService.registerUser(businessOwner);
        }

        // Create customers
        String[] customerNames = {"Alice Johnson", "Bob Smith", "Carol Davis", "Daniel Wilson", "Emma Brown", 
                                 "Frank Miller", "Grace Lee", "Henry Taylor", "Ivy Chen", "Jack Anderson"};
        
        for (int i = 0; i < customerNames.length; i++) {
            User customer = createUser(
                "customer_" + (i + 1),
                "customer" + (i + 1) + "@email.com",
                "987654323" + i,
                "password123",
                customerNames[i],
                20 + random.nextInt(20),
                "Residential Area, Mumbai",
                "DL00123458" + i,
                User.AccountType.CUSTOMER
            );
            userService.registerUser(customer);
        }

        // Create delivery partners
        String[] deliveryNames = {"Raj Kumar", "Amit Singh", "Vikram Patel", "Suresh Reddy"};
        
        for (int i = 0; i < deliveryNames.length; i++) {
            User deliveryPartner = createUser(
                "delivery_" + (i + 1),
                "delivery" + (i + 1) + "@partner.com",
                "987654324" + i,
                "password123",
                deliveryNames[i],
                25 + random.nextInt(15),
                "Delivery Hub, Mumbai",
                "DL00123459" + i,
                User.AccountType.DELIVERY_PARTNER
            );
            userService.registerUser(deliveryPartner);
        }
    }

    private User createUser(String username, String email, String phone, String password, 
                           String fullName, int age, String address, String drivingLicense, 
                           User.AccountType accountType) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setAge(age);
        user.setAddress(address);
        user.setDrivingLicense(drivingLicense);
        user.setAccountType(accountType);
        user.setCity(address.split(",")[1].trim());
        user.setState(address.split(",")[2].trim());
        return user;
    }

    private void initializeBikes() {
        List<User> owners = userService.getAllUsers().stream()
            .filter(user -> user.getAccountType() == User.AccountType.INDIVIDUAL_OWNER || 
                           user.getAccountType() == User.AccountType.BUSINESS_OWNER)
            .toList();

        String[] bikeBrands = {"Honda", "Bajaj", "TVS", "Yamaha", "Hero", "Royal Enfield", "KTM", "Suzuki"};
        String[] bikeModels = {"Activa", "Pulsar", "Jupiter", "R15", "Splendor", "Classic", "Duke", "Access"};
        String[] colors = {"Red", "Blue", "Black", "White", "Silver", "Green", "Yellow", "Orange"};
        String[] conditions = {"Excellent", "Good", "Very Good", "Fair", "Like New"};

        for (int i = 0; i < 50; i++) {
            Bike bike = new Bike();
            bike.setName(bikeBrands[i % bikeBrands.length] + " " + bikeModels[i % bikeModels.length]);
            bike.setBrand(bikeBrands[i % bikeBrands.length]);
            bike.setModel(bikeModels[i % bikeModels.length]);
            bike.setYear(2020 + random.nextInt(4));
            bike.setType(Bike.BikeType.values()[random.nextInt(Bike.BikeType.values().length)]);
            bike.setFuelType(Bike.FuelType.values()[random.nextInt(Bike.FuelType.values().length)]);
            bike.setEngineCapacity(100 + random.nextInt(200));
            bike.setColor(colors[i % colors.length]);
            bike.setRegistrationNumber("KA" + String.format("%02d", random.nextInt(99)) + 
                                     (char)(65 + random.nextInt(26)) + (char)(65 + random.nextInt(26)) + 
                                     String.format("%04d", random.nextInt(9999)));
            bike.setDailyRate(new BigDecimal(300 + random.nextInt(700)));
            bike.setMonthlyRate(bike.getDailyRate().multiply(new BigDecimal(25)));
            bike.setHourlyRate(bike.getDailyRate().divide(new BigDecimal(8), 2, BigDecimal.ROUND_HALF_UP));
            bike.setLocation("Mumbai, Maharashtra");
            bike.setDescription("Well-maintained " + bike.getName() + " with low mileage. Perfect for city rides.");
            bike.setHasHelmet(random.nextBoolean());
            bike.setHasNavigation(random.nextBoolean());
            bike.setIsInsured(true);
            bike.setMileage(random.nextInt(50000));
            bike.setCondition(conditions[random.nextInt(conditions.length)]);
            bike.setAverageRating(3.5 + random.nextDouble() * 1.5);
            bike.setTotalReviews(random.nextInt(50));
            bike.setOwner(owners.get(random.nextInt(owners.size())));
            bikeService.addBike(bike);
        }
    }

    private void initializeBookings() {
        List<User> customers = userService.getAllUsers().stream()
            .filter(user -> user.getAccountType() == User.AccountType.CUSTOMER)
            .toList();
        List<Bike> bikes = bikeService.getAllBikes();

        for (int i = 0; i < 30; i++) {
            Booking booking = new Booking();
            booking.setUser(customers.get(random.nextInt(customers.size())));
            booking.setBike(bikes.get(random.nextInt(bikes.size())));
            booking.setStartTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            booking.setEndTime(booking.getStartTime().plusHours(1 + random.nextInt(72)));
            booking.setBookingType(Booking.BookingType.values()[random.nextInt(Booking.BookingType.values().length)]);
            booking.setStatus(Booking.BookingStatus.values()[random.nextInt(Booking.BookingStatus.values().length)]);
            booking.setPaymentStatus(Booking.PaymentStatus.values()[random.nextInt(Booking.PaymentStatus.values().length)]);
            booking.setTotalAmount(booking.getBike().getDailyRate().multiply(new BigDecimal(booking.getEndTime().getHour() - booking.getStartTime().getHour())));
            booking.setPickupLocation("Mumbai Central");
            booking.setDropoffLocation("Mumbai Central");
            bookingService.createBooking(booking);
        }
    }

    private void initializePayments() {
        List<Booking> bookings = bookingService.getAllBookings();

        for (Booking booking : bookings) {
            if (booking.getPaymentStatus() == Booking.PaymentStatus.COMPLETED) {
                Payment payment = new Payment();
                payment.setBooking(booking);
                payment.setAmount(booking.getTotalAmount());
                payment.setPaymentMethod(Payment.PaymentMethod.values()[random.nextInt(Payment.PaymentMethod.values().length)]);
                payment.setStatus(Payment.PaymentStatus.SUCCESS);
                payment.setTransactionId("TXN" + System.currentTimeMillis() + random.nextInt(1000));
                payment.setPaymentDate(booking.getStartTime().minusHours(1));
                paymentService.processPayment(payment);
            }
        }
    }

    private void initializeReviews() {
        List<Booking> completedBookings = bookingService.getAllBookings().stream()
            .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
            .toList();

        String[] reviewComments = {
            "Great bike, very smooth ride!",
            "Excellent service, highly recommended.",
            "Good condition bike, will book again.",
            "Perfect for city rides, fuel efficient.",
            "Clean bike, good customer service.",
            "Amazing experience, bike was in top condition.",
            "Quick pickup and drop, convenient service.",
            "Good value for money, reliable bike.",
            "Professional service, bike was well maintained.",
            "Easy booking process, bike was as described."
        };

        for (int i = 0; i < Math.min(20, completedBookings.size()); i++) {
            Booking booking = completedBookings.get(i);
            Review review = new Review();
            review.setUser(booking.getUser());
            review.setBike(booking.getBike());
            review.setBooking(booking);
            review.setRating(3 + random.nextInt(3));
            review.setComment(reviewComments[random.nextInt(reviewComments.length)]);
            review.setCreatedAt(booking.getEndTime().plusDays(random.nextInt(7)));
            reviewService.addReview(review);
        }
    }

    private void initializeNotifications() {
        List<User> users = userService.getAllUsers();

        String[] notificationMessages = {
            "Welcome to SpinGO! Start exploring bikes near you.",
            "Your booking has been confirmed.",
            "Your bike is ready for pickup.",
            "Payment successful for your booking.",
            "Thank you for your review!",
            "New bikes available in your area.",
            "Special offer: 20% off on weekend bookings.",
            "Your booking has been completed successfully.",
            "Rate your recent ride experience.",
            "Maintenance scheduled for your bike."
        };

        for (int i = 0; i < 50; i++) {
            Notification notification = new Notification();
            notification.setUser(users.get(random.nextInt(users.size())));
            notification.setTitle("SpinGO Notification");
            notification.setMessage(notificationMessages[random.nextInt(notificationMessages.length)]);
            notification.setType(Notification.NotificationType.values()[random.nextInt(Notification.NotificationType.values().length)]);
            notification.setStatus(Notification.NotificationStatus.values()[random.nextInt(Notification.NotificationStatus.values().length)]);
            notification.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            notificationService.createNotification(notification);
        }
    }
}
