package com.spingo.repository;

import com.spingo.entity.Bike;
import com.spingo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    
    List<Bike> findByStatus(Bike.BikeStatus status);
    List<Bike> findByOwner(User owner);
    List<Bike> findByType(Bike.BikeType type);
    List<Bike> findByFuelType(Bike.FuelType fuelType);
    List<Bike> findByLocation(String location);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE'")
    List<Bike> findAvailableBikes();
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' AND b.location = :location")
    List<Bike> findAvailableBikesByLocation(@Param("location") String location);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' AND b.dailyRate <= :maxRate")
    List<Bike> findAvailableBikesByMaxRate(@Param("maxRate") BigDecimal maxRate);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' AND b.type = :type AND b.location = :location")
    List<Bike> findAvailableBikesByTypeAndLocation(@Param("type") Bike.BikeType type, @Param("location") String location);
    
    @Query("SELECT COUNT(b) FROM Bike b WHERE b.status = 'AVAILABLE'")
    long countAvailableBikes();
    
    @Query("SELECT COUNT(b) FROM Bike b WHERE b.owner = :owner")
    long countBikesByOwner(@Param("owner") User owner);
}
