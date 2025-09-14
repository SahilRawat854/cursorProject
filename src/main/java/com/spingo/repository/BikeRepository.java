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
    
    // Advanced search queries
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "AND (:city IS NULL OR b.location LIKE %:city%) " +
           "AND (:bikeType IS NULL OR b.type = :bikeType) " +
           "AND (:fuelType IS NULL OR b.fuelType = :fuelType) " +
           "AND (:minPrice IS NULL OR b.dailyRate >= :minPrice) " +
           "AND (:maxPrice IS NULL OR b.dailyRate <= :maxPrice) " +
           "AND (:minYear IS NULL OR b.year >= :minYear) " +
           "AND (:maxYear IS NULL OR b.year <= :maxYear) " +
           "AND (:hasHelmet IS NULL OR b.hasHelmet = :hasHelmet) " +
           "AND (:hasNavigation IS NULL OR b.hasNavigation = :hasNavigation) " +
           "AND (:isInsured IS NULL OR b.isInsured = :isInsured) " +
           "AND (:minRating IS NULL OR b.averageRating >= :minRating)")
    List<Bike> findBikesByAdvancedSearch(@Param("city") String city, 
                                        @Param("bikeType") String bikeType,
                                        @Param("fuelType") String fuelType,
                                        @Param("minPrice") BigDecimal minPrice,
                                        @Param("maxPrice") BigDecimal maxPrice,
                                        @Param("minYear") Integer minYear,
                                        @Param("maxYear") Integer maxYear,
                                        @Param("hasHelmet") Boolean hasHelmet,
                                        @Param("hasNavigation") Boolean hasNavigation,
                                        @Param("isInsured") Boolean isInsured,
                                        @Param("minRating") Double minRating);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE'")
    List<Bike> findBikesNearLocation(@Param("minLat") Double minLat, 
                                   @Param("maxLat") Double maxLat,
                                   @Param("minLng") Double minLng, 
                                   @Param("maxLng") Double maxLng);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "ORDER BY b.totalReviews DESC, b.averageRating DESC")
    List<Bike> findPopularBikes();
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "AND b.averageRating >= 4.0 " +
           "ORDER BY b.averageRating DESC, b.totalReviews DESC")
    List<Bike> findRecommendedBikes();
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "AND b.averageRating >= :minRating " +
           "ORDER BY b.averageRating DESC")
    List<Bike> findBikesByMinRating(@Param("minRating") Double minRating);
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "ORDER BY b.createdAt DESC")
    List<Bike> findRecentlyAddedBikes();
    
    @Query("SELECT b FROM Bike b WHERE b.status = 'AVAILABLE' " +
           "AND b.dailyRate BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY b.dailyRate ASC")
    List<Bike> findBikesByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);
}
