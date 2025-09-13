@echo off
echo Starting SpinGO Bike Rental Application...
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo.
echo Building the application...
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Error: Build failed
    pause
    exit /b 1
)

echo.
echo Starting the application...
echo The application will be available at: http://localhost:8080
echo.
echo Default admin credentials:
echo Username: admin
echo Password: admin123
echo.
echo Press Ctrl+C to stop the application
echo.

mvn spring-boot:run

pause
