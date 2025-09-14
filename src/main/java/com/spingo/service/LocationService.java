package com.spingo.service;

import com.spingo.entity.Location;
import com.spingo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    public Location createLocation(String name, String address, String city, String state, 
                                 String pincode, Double latitude, Double longitude, 
                                 Location.LocationType type) {
        Location location = new Location(name, address, city, state, pincode, latitude, longitude, type);
        return locationRepository.save(location);
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    public List<Location> getActiveLocations() {
        return locationRepository.findByTypeAndStatus(Location.LocationType.PICKUP_POINT, 
            Location.LocationStatus.ACTIVE);
    }
    
    public List<Location> getLocationsByCity(String city) {
        return locationRepository.findByCityAndStatus(city, Location.LocationStatus.ACTIVE);
    }
    
    public List<Location> getPickupPointsByCity(String city) {
        return locationRepository.findActiveLocationsByCityAndType(city, Location.LocationType.PICKUP_POINT);
    }
    
    public List<Location> getAvailableLocations() {
        return locationRepository.findAvailableLocations();
    }
    
    public List<String> getAvailableCities() {
        return locationRepository.findDistinctActiveCities();
    }
    
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    public Location updateLocation(Location location) {
        location.setUpdatedAt(LocalDateTime.now());
        return locationRepository.save(location);
    }
    
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
    
    public Location updateLocationStatus(Long id, Location.LocationStatus status) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        location.setStatus(status);
        location.setUpdatedAt(LocalDateTime.now());
        
        return locationRepository.save(location);
    }
    
    public Location updateAvailableSlots(Long id, Integer availableSlots) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        if (availableSlots < 0 || availableSlots > location.getCapacity()) {
            throw new RuntimeException("Invalid number of available slots");
        }
        
        location.setAvailableSlots(availableSlots);
        location.setUpdatedAt(LocalDateTime.now());
        
        return locationRepository.save(location);
    }
    
    public void decrementAvailableSlots(Long id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        if (location.getAvailableSlots() > 0) {
            location.setAvailableSlots(location.getAvailableSlots() - 1);
            location.setUpdatedAt(LocalDateTime.now());
            locationRepository.save(location);
        }
    }
    
    public void incrementAvailableSlots(Long id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        if (location.getAvailableSlots() < location.getCapacity()) {
            location.setAvailableSlots(location.getAvailableSlots() + 1);
            location.setUpdatedAt(LocalDateTime.now());
            locationRepository.save(location);
        }
    }
}
