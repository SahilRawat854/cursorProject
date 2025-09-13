#!/bin/bash

echo "Starting SpinGO Bike Rental Application..."
echo

echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

java -version

echo
echo "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

mvn -version

echo
echo "Building the application..."
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "Error: Build failed"
    exit 1
fi

echo
echo "Starting the application..."
echo "The application will be available at: http://localhost:8080"
echo
echo "Default admin credentials:"
echo "Username: admin"
echo "Password: admin123"
echo
echo "Press Ctrl+C to stop the application"
echo

mvn spring-boot:run
