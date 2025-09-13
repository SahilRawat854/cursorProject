-- SpinGO Bike Rental Application Database Setup
-- Run this script to create the database and user

-- Create database
CREATE DATABASE IF NOT EXISTS bike_rental_db;


-- Create user (optional - you can use root user)
CREATE USER IF NOT EXISTS 'spingo_user'@'localhost' IDENTIFIED BY 'spingo_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON bike_rental_db.* TO 'spingo_user'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;

-- Use the database
USE bike_rental_db;

-- Note: Tables will be automatically created by Hibernate when the application starts
-- The following tables will be created:
-- - users
-- - bikes  
-- - bookings
-- - payments

-- Sample data will be automatically inserted by the DataInitializer class
-- when the application starts for the first time.

-- Default admin credentials:
-- Username: admin
-- Password: admin123

-- Sample user credentials:
-- Username: john_owner (Individual Owner)
-- Username: jane_customer (Customer)
-- Username: bike_rental_co (Business Owner)
-- Username: delivery_partner (Delivery Partner)
-- Password for all sample users: password123
