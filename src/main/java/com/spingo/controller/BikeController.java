package com.spingo.controller;

import com.spingo.entity.Bike;
import com.spingo.entity.User;
import com.spingo.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/bikes")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public String listBikes(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal maxRate,
            Model model) {
        
        List<Bike> bikes;
        
        if (location != null && type != null) {
            bikes = bikeService.getAvailableBikesByTypeAndLocation(
                Bike.BikeType.valueOf(type.toUpperCase()), location);
        } else if (location != null) {
            bikes = bikeService.getAvailableBikesByLocation(location);
        } else if (type != null) {
            bikes = bikeService.getBikesByType(Bike.BikeType.valueOf(type.toUpperCase()));
        } else if (maxRate != null) {
            bikes = bikeService.getBikesByMaxRate(maxRate);
        } else {
            bikes = bikeService.getAvailableBikes();
        }
        
        model.addAttribute("bikes", bikes);
        model.addAttribute("bikeTypes", Bike.BikeType.values());
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedMaxRate", maxRate);
        
        return "bikes/list";
    }

    @GetMapping("/{id}")
    public String viewBike(@PathVariable Long id, Model model) {
        Bike bike = bikeService.findById(id)
            .orElseThrow(() -> new RuntimeException("Bike not found"));
        
        model.addAttribute("bike", bike);
        return "bikes/details";
    }

    @GetMapping("/add")
    public String addBikeForm(Model model) {
        model.addAttribute("bike", new Bike());
        model.addAttribute("bikeTypes", Bike.BikeType.values());
        model.addAttribute("fuelTypes", Bike.FuelType.values());
        return "bikes/add";
    }

    @PostMapping("/add")
    public String addBike(@ModelAttribute Bike bike, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User owner = (User) authentication.getPrincipal();
            bike.setOwner(owner);
            
            bikeService.addBike(bike);
            redirectAttributes.addFlashAttribute("success", "Bike added successfully!");
            return "redirect:/owner/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding bike: " + e.getMessage());
            return "redirect:/bikes/add";
        }
    }

    @GetMapping("/{id}/edit")
    public String editBikeForm(@PathVariable Long id, Model model) {
        Bike bike = bikeService.findById(id)
            .orElseThrow(() -> new RuntimeException("Bike not found"));
        
        model.addAttribute("bike", bike);
        model.addAttribute("bikeTypes", Bike.BikeType.values());
        model.addAttribute("fuelTypes", Bike.FuelType.values());
        return "bikes/edit";
    }

    @PostMapping("/{id}/edit")
    public String editBike(@PathVariable Long id, @ModelAttribute Bike bike, RedirectAttributes redirectAttributes) {
        try {
            Bike existingBike = bikeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found"));
            
            // Update fields
            existingBike.setName(bike.getName());
            existingBike.setBrand(bike.getBrand());
            existingBike.setModel(bike.getModel());
            existingBike.setYear(bike.getYear());
            existingBike.setType(bike.getType());
            existingBike.setFuelType(bike.getFuelType());
            existingBike.setEngineCapacity(bike.getEngineCapacity());
            existingBike.setColor(bike.getColor());
            existingBike.setDailyRate(bike.getDailyRate());
            existingBike.setMonthlyRate(bike.getMonthlyRate());
            existingBike.setHourlyRate(bike.getHourlyRate());
            existingBike.setLocation(bike.getLocation());
            existingBike.setDescription(bike.getDescription());
            existingBike.setHasHelmet(bike.getHasHelmet());
            existingBike.setHasNavigation(bike.getHasNavigation());
            existingBike.setIsInsured(bike.getIsInsured());
            
            bikeService.updateBike(existingBike);
            redirectAttributes.addFlashAttribute("success", "Bike updated successfully!");
            return "redirect:/owner/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating bike: " + e.getMessage());
            return "redirect:/bikes/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBike(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bikeService.deleteBike(id);
            redirectAttributes.addFlashAttribute("success", "Bike deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting bike: " + e.getMessage());
        }
        return "redirect:/owner/dashboard";
    }
}
