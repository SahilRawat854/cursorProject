# SpinGO - Bike Rental Application

A comprehensive bike rental platform built with Spring Boot, Thymeleaf, JPA, and MySQL. This application provides a complete solution for bike rental services with features for customers, bike owners, delivery partners, and administrators.

## Features

### 🚴‍♂️ Customer Features
- User registration with email/phone and driving license verification
- Browse and search available bikes by location, type, and price
- Book bikes with flexible rental durations (hourly, daily, monthly)
- Add helmet and navigation services
- Online payment integration
- View booking history and manage active bookings
- Extend or cancel bookings

### 🏢 Business Owner Features
- Register as individual or business owner
- Add and manage bike listings
- Set custom pricing based on demand and time
- Track bike usage and earnings
- Monitor bookings and rental history
- Bulk-rent bikes for delivery fleet

### 🚚 Delivery Partner Features
- Authenticate before booking delivery vehicles
- Track delivery vehicles in real-time
- Receive alerts when vehicle reaches location
- Support cab and package delivery workflows

### 👨‍💼 Admin Features
- View all users and their bookings
- Add, update, or remove bikes
- Monitor user disputes and resolve them
- Analyze rental patterns and user behavior
- Manage payments and refunds
- Offer rewards to responsible users

## Tech Stack

- **Backend**: Spring Boot 3.2.0
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Database**: MySQL 8.0
- **Security**: Spring Security
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd bike-rental-app
```

### 2. Database Setup
1. Create a MySQL database named `bike_rental_db`
2. Update the database credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bike_rental_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### 4. Default Admin Credentials
- Username: `admin`
- Password: `admin123`

## Project Structure

```
src/
├── main/
│   ├── java/com/spingo/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   └── BikeRentalApplication.java
│   └── resources/
│       ├── static/          # Static assets (CSS, JS, images)
│       ├── templates/       # Thymeleaf templates
│       └── application.yml  # Application configuration
└── test/                    # Test files
```

## Key Features Implementation

### Authentication & Authorization
- Spring Security integration
- Role-based access control (Customer, Owner, Delivery Partner, Admin)
- Password encryption with BCrypt
- Session management

### Database Design
- User management with multiple account types
- Bike catalog with detailed specifications
- Booking system with flexible rental periods
- Payment tracking and management
- Comprehensive audit trails

### UI/UX Features
- Responsive design matching the provided mockups
- Dark theme with modern styling
- Interactive JavaScript components
- Real-time form validation
- Smooth animations and transitions

### Payment Integration
- Dummy payment gateway implementation
- Support for multiple payment methods (UPI, Cards, Net Banking, Wallets, Cash)
- Payment status tracking
- Refund management

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `GET /logout` - Logout

### Bike Management
- `GET /bikes` - List all bikes
- `GET /bikes/{id}` - View bike details
- `GET /bikes/add` - Add bike form
- `POST /bikes/add` - Create new bike
- `GET /bikes/{id}/edit` - Edit bike form
- `POST /bikes/{id}/edit` - Update bike
- `POST /bikes/{id}/delete` - Delete bike

### Booking Management
- `GET /bookings` - List user bookings
- `GET /bookings/new/{bikeId}` - New booking form
- `POST /bookings/new` - Create booking
- `GET /bookings/{id}` - View booking details
- `POST /bookings/{id}/cancel` - Cancel booking
- `POST /bookings/{id}/extend` - Extend booking

### Payment Processing
- `GET /payments/{bookingId}` - Payment page
- `POST /payments/process` - Process payment
- `GET /payments/success/{transactionId}` - Payment success
- `GET /payments/failure/{transactionId}` - Payment failure

### Admin Panel
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/users` - Manage users
- `GET /admin/bikes` - Manage bikes
- `GET /admin/bookings` - Manage bookings
- `GET /admin/payments` - Manage payments
- `GET /admin/analytics` - View analytics

## Configuration

### Application Properties
Key configuration options in `application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Automatically create/update database schema
    show-sql: true      # Show SQL queries in console
  
  security:
    user:
      name: admin       # Default admin username
      password: admin123 # Default admin password
```

### Database Configuration
The application uses JPA with Hibernate for database operations. Tables are automatically created based on entity definitions.

## Development

### Adding New Features
1. Create entity classes in `com.spingo.entity`
2. Create repository interfaces in `com.spingo.repository`
3. Implement service classes in `com.spingo.service`
4. Create controllers in `com.spingo.controller`
5. Add Thymeleaf templates in `src/main/resources/templates`
6. Update CSS/JS files in `src/main/resources/static`

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName

# Run with coverage
mvn test jacoco:report
```

## Deployment

### Production Configuration
1. Update `application.yml` for production database
2. Set proper security configurations
3. Configure email settings for notifications
4. Set up proper logging configuration
5. Use external configuration files for sensitive data

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/bike-rental-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please contact the development team or create an issue in the repository.

## Future Enhancements

- Real-time GPS tracking
- Mobile app development
- Advanced analytics and reporting
- Integration with actual payment gateways
- Push notifications
- Multi-language support
- Advanced search and filtering
- Social features and reviews
