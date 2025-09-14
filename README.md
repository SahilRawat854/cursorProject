# SpinGO - Bike Rental Application 🚴‍♂️

A modern, full-featured bike rental platform built with Spring Boot, featuring a stunning dark theme with purple and magenta gradients. This application provides a complete solution for bike rental services with features for customers, bike owners, delivery partners, and administrators.

## ✨ Features

### 🎨 **Modern Dark Theme UI/UX**
- **Purple & Magenta Gradient Design**: Beautiful dark theme with purple (#8B5CF6) and magenta (#EC4899) gradients
- **Responsive Design**: Fully responsive across all devices
- **Smooth Animations**: CSS transitions and hover effects
- **Modern Typography**: Inter font family for clean, readable text
- **Glassmorphism Effects**: Backdrop blur and transparency effects

### 🚴‍♂️ **Customer Features**
- User registration with email/phone and driving license verification
- Browse and search available bikes by location, type, and price
- Book bikes with flexible rental durations (hourly, daily, monthly)
- Add helmet and navigation services
- Online payment integration
- View booking history and manage active bookings
- Extend or cancel bookings

### 🏢 **Business Owner Features**
- Register as individual or business owner
- Add and manage bike listings
- Set custom pricing based on demand and time
- Track bike usage and earnings
- Monitor bookings and rental history
- Bulk-rent bikes for delivery fleet

### 🚚 **Delivery Partner Features**
- Authenticate before booking delivery vehicles
- Track delivery vehicles in real-time
- Receive alerts when vehicle reaches location
- Support cab and package delivery workflows

### 👨‍💼 **Admin Features**
- View all users and their bookings
- Add, update, or remove bikes
- Monitor user disputes and resolve them
- Analyze rental patterns and user behavior
- Manage payments and refunds
- Offer rewards to responsible users

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.0
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Database**: MySQL 8.0
- **Security**: Spring Security
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher  
- **MySQL 8.0** or higher
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Quick Start Guide

### 1. **Clone and Setup**
```bash
# Clone the repository
git clone <repository-url>
cd bike-rental-app

# Navigate to project directory
cd cursorProject
```

### 2. **Database Setup**

#### Option A: Using MySQL Command Line
```bash
# Start MySQL service
# On Windows: Start MySQL service from Services or use XAMPP/WAMP
# On Linux/Mac: sudo systemctl start mysql

# Connect to MySQL and create database
mysql -u root -p
CREATE DATABASE bike_rental_db;
exit
```

#### Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Create a new schema named `bike_rental_db`
4. Set character set to `utf8mb4` and collation to `utf8mb4_unicode_ci`

### 3. **Configure Database Connection**

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bike_rental_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    username: your_username  # Change this
    password: your_password  # Change this
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. **Run the Application**

#### Option A: Using Maven (Recommended)
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

#### Option B: Using IDE
1. Open the project in your IDE
2. Run the `BikeRentalApplication.java` main class

#### Option C: Using Batch File (Windows)
```bash
# Double-click start.bat or run in command prompt
start.bat
```

### 5. **Access the Application**
- Open your browser and go to: `http://localhost:8080`
- The application will be running on port 8080

## 🔐 Default Login Credentials

### **Admin Account**
- **Username**: `admin`
- **Password**: `admin123`

### **Sample User Accounts**
- **Username**: `owner_1` (Individual Owner) - Password: `password123`
- **Username**: `business_1` (Business Owner) - Password: `password123`
- **Username**: `customer_1` (Customer) - Password: `password123`
- **Username**: `delivery_1` (Delivery Partner) - Password: `password123`

## 📊 Dummy Database Data

The application automatically populates the database with comprehensive dummy data:

### **Users (25+ users)**
- 1 Admin user
- 6 Individual bike owners
- 5 Business owners
- 10 Customers
- 4 Delivery partners

### **Bikes (50+ bikes)**
- Various brands: Honda, Bajaj, TVS, Yamaha, Hero, Royal Enfield, KTM, Suzuki
- Different types: Scooter, Motorcycle, Electric Bike, Sports Bike, Cruiser
- Random pricing: ₹300-₹1000 per day
- Various conditions: Excellent, Good, Very Good, Fair, Like New

### **Bookings (30+ bookings)**
- Different booking types: Hourly, Daily, Monthly
- Various statuses: Confirmed, Active, Completed, Cancelled
- Payment statuses: Pending, Completed, Failed

### **Payments**
- Multiple payment methods: UPI, Cards, Net Banking, Wallets, Cash
- Transaction IDs and payment dates

### **Reviews (20+ reviews)**
- Ratings from 3-5 stars
- Realistic review comments
- Associated with completed bookings

### **Locations (24+ cities)**
- Major Indian cities with pickup/drop locations
- GPS coordinates and available slots

### **Notifications (50+ notifications)**
- Various notification types: Booking, Payment, System, Promotion
- Different statuses: Read, Unread

## 🔧 Database Customization Guide

### **Replacing with Your Own Database**

#### 1. **Update Database Configuration**
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://your-host:3306/your-database-name?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 2. **Disable Dummy Data Initialization**
To prevent dummy data from being created, comment out the `@Component` annotation in `DataInitializer.java`:

```java
// @Component  // Comment this line
public class DataInitializer implements CommandLineRunner {
    // ... rest of the code
}
```

#### 3. **Customize Data Initialization**
Modify the `DataInitializer.java` file to create your own initial data:

```java
@Override
public void run(String... args) throws Exception {
    // Only initialize data if no users exist
    if (userService.getAllUsers().isEmpty()) {
        // Your custom initialization logic here
        initializeYourCustomUsers();
        initializeYourCustomBikes();
        // ... other custom data
    }
}
```

#### 4. **Database Schema Customization**
The application uses JPA with Hibernate, so tables are automatically created. To customize:

1. **Modify Entity Classes**: Update `src/main/java/com/spingo/entity/` files
2. **Update Repositories**: Modify `src/main/java/com/spingo/repository/` files
3. **Custom Queries**: Add custom SQL queries in repository interfaces

### **Production Database Setup**

#### 1. **Production Configuration**
Create `src/main/resources/application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://your-production-host:3306/bike_rental_prod
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate  # Use validate in production
    show-sql: false      # Disable SQL logging in production

logging:
  level:
    com.spingo: INFO     # Reduce logging level
    org.springframework.security: WARN
```

#### 2. **Environment Variables**
Set these environment variables:
```bash
export DB_USERNAME=your_production_username
export DB_PASSWORD=your_production_password
```

#### 3. **Run with Production Profile**
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 🎨 UI/UX Customization

### **Color Scheme**
The application uses CSS variables for easy customization. Update `src/main/resources/static/css/style.css`:

```css
:root {
    /* Purple & Magenta Color Palette */
    --primary-purple: #8B5CF6;      /* Change this */
    --primary-magenta: #EC4899;     /* Change this */
    --secondary-purple: #A855F7;    /* Change this */
    --secondary-magenta: #F472B6;   /* Change this */
    
    /* Dark Theme Colors */
    --bg-primary: #0F0F23;          /* Change this */
    --bg-secondary: #1A1A2E;        /* Change this */
    --bg-tertiary: #16213E;         /* Change this */
    
    /* Text Colors */
    --text-primary: #FFFFFF;        /* Change this */
    --text-secondary: #B8B8D1;      /* Change this */
}
```

### **Adding Background Images**
1. Add images to `src/main/resources/static/images/`
2. Update CSS to include background images:

```css
body::before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: 
        url('/images/background-pattern.png'),
        radial-gradient(circle at 20% 80%, rgba(139, 92, 246, 0.1) 0%, transparent 50%);
    pointer-events: none;
    z-index: -1;
}
```

## 🐛 Troubleshooting

### **Common Issues**

#### 1. **Database Connection Error**
```
Error: Could not create connection to database server
```
**Solution**: 
- Check MySQL service is running
- Verify database credentials in `application.yml`
- Ensure database `bike_rental_db` exists

#### 2. **Port Already in Use**
```
Error: Port 8080 was already in use
```
**Solution**: 
- Change port in `application.yml`: `server.port: 8081`
- Or stop other applications using port 8080

#### 3. **Java Version Error**
```
Error: Unsupported major.minor version
```
**Solution**: 
- Ensure Java 17+ is installed
- Check JAVA_HOME environment variable
- Update IDE Java version settings

#### 4. **Maven Build Error**
```
Error: Failed to execute goal
```
**Solution**: 
- Run `mvn clean install -U` to force update dependencies
- Check internet connection for dependency downloads
- Clear Maven cache: `mvn dependency:purge-local-repository`

### **Performance Optimization**

#### 1. **Database Optimization**
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

#### 2. **Caching**
```yaml
spring:
  cache:
    type: caffeine
  caffeine:
    spec: maximumSize=500,expireAfterWrite=5m
```

## 📱 Mobile Responsiveness

The application is fully responsive and optimized for:
- **Desktop**: 1200px+ screens
- **Tablet**: 768px - 1199px screens  
- **Mobile**: 320px - 767px screens

## 🔒 Security Features

- **Password Encryption**: BCrypt password hashing
- **Session Management**: Secure session handling
- **CSRF Protection**: Cross-site request forgery protection
- **Input Validation**: Server-side validation for all inputs
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries

## 📈 Monitoring & Analytics

The application includes:
- **Actuator Endpoints**: Health checks and metrics
- **Logging**: Comprehensive logging with different levels
- **Error Handling**: Global exception handling
- **Performance Metrics**: Built-in performance monitoring

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the troubleshooting section above

## 🚀 Deployment

### **Docker Deployment**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/bike-rental-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **Cloud Deployment**
The application is ready for deployment on:
- **AWS**: EC2, Elastic Beanstalk, RDS
- **Google Cloud**: Compute Engine, Cloud SQL
- **Azure**: App Service, SQL Database
- **Heroku**: With MySQL addon

---

**Enjoy your bike rental platform! 🚴‍♂️✨**