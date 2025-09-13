package com.spingo.service;

import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BikeService {

    @Autowired
    private BikeRepository bikeRepository;

    public Bike addBike(Bike bike) {
        return bikeRepository.save(bike);
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public List<Bike> getAvailableBikes() {
        return bikeRepository.findAvailableBikes();
    }

    public List<Bike> getAvailableBikesByLocation(String location) {
        return bikeRepository.findAvailableBikesByLocation(location);
    }

    public List<Bike> getAvailableBikesByTypeAndLocation(Bike.BikeType type, String location) {
        return bikeRepository.findAvailableBikesByTypeAndLocation(type, location);
    }

    public List<Bike> getBikesByOwner(User owner) {
        return bikeRepository.findByOwner(owner);
    }

    public List<Bike> getBikesByType(Bike.BikeType type) {
        return bikeRepository.findByType(type);
    }

    public List<Bike> getBikesByMaxRate(BigDecimal maxRate) {
        return bikeRepository.findAvailableBikesByMaxRate(maxRate);
    }

    public Optional<Bike> findById(Long id) {
        return bikeRepository.findById(id);
    }

    public Bike updateBike(Bike bike) {
        return bikeRepository.save(bike);
    }

    public void deleteBike(Long id) {
        bikeRepository.deleteById(id);
    }

    public long getAvailableBikeCount() {
        return bikeRepository.countAvailableBikes();
    }

    public long getBikeCountByOwner(User owner) {
        return bikeRepository.countBikesByOwner(owner);
    }

    public List<Bike> searchBikes(String location, Bike.BikeType type, BigDecimal maxRate) {
        if (location != null && type != null) {
            return getAvailableBikesByTypeAndLocation(type, location);
        } else if (location != null) {
            return getAvailableBikesByLocation(location);
        } else if (type != null) {
            return getBikesByType(type);
        } else if (maxRate != null) {
            return getBikesByMaxRate(maxRate);
        } else {
            return getAvailableBikes();
        }
    }
}
