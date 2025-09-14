package com.spingo.repository;

import com.spingo.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    List<Location> findByCityAndStatus(String city, Location.LocationStatus status);
    
    List<Location> findByTypeAndStatus(Location.LocationType type, Location.LocationStatus status);
    
    @Query("SELECT l FROM Location l WHERE l.city = :city AND l.type = :type AND l.status = 'ACTIVE'")
    List<Location> findActiveLocationsByCityAndType(@Param("city") String city, @Param("type") Location.LocationType type);
    
    @Query("SELECT DISTINCT l.city FROM Location l WHERE l.status = 'ACTIVE' ORDER BY l.city")
    List<String> findDistinctActiveCities();
    
    @Query("SELECT l FROM Location l WHERE l.status = 'ACTIVE' AND l.availableSlots > 0")
    List<Location> findAvailableLocations();
}
