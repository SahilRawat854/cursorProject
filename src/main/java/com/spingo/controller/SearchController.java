package com.spingo.controller;

import com.spingo.entity.Bike;
import com.spingo.entity.Location;
import com.spingo.service.BikeService;
import com.spingo.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private BikeService bikeService;
    
    @Autowired
    private LocationService locationService;
    
    @GetMapping
    public String searchPage(Model model) {
        List<String> cities = locationService.getAvailableCities();
        model.addAttribute("cities", cities);
        return "search/advanced-search";
    }
    
    @PostMapping("/bikes")
    public String searchBikes(@RequestParam(required = false) String city,
                            @RequestParam(required = false) String bikeType,
                            @RequestParam(required = false) String fuelType,
                            @RequestParam(required = false) BigDecimal minPrice,
                            @RequestParam(required = false) BigDecimal maxPrice,
                            @RequestParam(required = false) Integer minYear,
                            @RequestParam(required = false) Integer maxYear,
                            @RequestParam(required = false) Boolean hasHelmet,
                            @RequestParam(required = false) Boolean hasNavigation,
                            @RequestParam(required = false) Boolean isInsured,
                            @RequestParam(required = false) Double minRating,
                            Model model) {
        
        List<Bike> bikes = bikeService.searchBikes(city, bikeType, fuelType, minPrice, maxPrice, 
                                                 minYear, maxYear, hasHelmet, hasNavigation, 
                                                 isInsured, minRating);
        
        model.addAttribute("bikes", bikes);
        model.addAttribute("searchParams", new SearchParams(city, bikeType, fuelType, minPrice, 
                                                           maxPrice, minYear, maxYear, hasHelmet, 
                                                           hasNavigation, isInsured, minRating));
        
        return "search/results";
    }
    
    @GetMapping("/nearby")
    public String searchNearby(@RequestParam Double latitude,
                             @RequestParam Double longitude,
                             @RequestParam(defaultValue = "10") Double radius,
                             Model model) {
        
        List<Bike> nearbyBikes = bikeService.findBikesNearLocation(latitude, longitude, radius);
        
        model.addAttribute("bikes", nearbyBikes);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("radius", radius);
        
        return "search/nearby-results";
    }
    
    @GetMapping("/popular")
    public String getPopularBikes(Model model) {
        List<Bike> popularBikes = bikeService.getPopularBikes();
        model.addAttribute("bikes", popularBikes);
        return "search/popular-bikes";
    }
    
    @GetMapping("/recommended")
    public String getRecommendedBikes(Model model) {
        List<Bike> recommendedBikes = bikeService.getRecommendedBikes();
        model.addAttribute("bikes", recommendedBikes);
        return "search/recommended-bikes";
    }
    
    // Helper class for search parameters
    public static class SearchParams {
        private String city;
        private String bikeType;
        private String fuelType;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Integer minYear;
        private Integer maxYear;
        private Boolean hasHelmet;
        private Boolean hasNavigation;
        private Boolean isInsured;
        private Double minRating;
        
        public SearchParams(String city, String bikeType, String fuelType, BigDecimal minPrice,
                          BigDecimal maxPrice, Integer minYear, Integer maxYear, Boolean hasHelmet,
                          Boolean hasNavigation, Boolean isInsured, Double minRating) {
            this.city = city;
            this.bikeType = bikeType;
            this.fuelType = fuelType;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minYear = minYear;
            this.maxYear = maxYear;
            this.hasHelmet = hasHelmet;
            this.hasNavigation = hasNavigation;
            this.isInsured = isInsured;
            this.minRating = minRating;
        }
        
        // Getters
        public String getCity() { return city; }
        public String getBikeType() { return bikeType; }
        public String getFuelType() { return fuelType; }
        public BigDecimal getMinPrice() { return minPrice; }
        public BigDecimal getMaxPrice() { return maxPrice; }
        public Integer getMinYear() { return minYear; }
        public Integer getMaxYear() { return maxYear; }
        public Boolean getHasHelmet() { return hasHelmet; }
        public Boolean getHasNavigation() { return hasNavigation; }
        public Boolean getIsInsured() { return isInsured; }
        public Double getMinRating() { return minRating; }
    }
}
