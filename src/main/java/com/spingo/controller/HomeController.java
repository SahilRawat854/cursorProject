package com.spingo.controller;

import com.spingo.service.BikeService;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        // Add statistics for the landing page
        model.addAttribute("totalUsers", userService.getActiveUserCount());
        model.addAttribute("totalBikes", bikeService.getAvailableBikeCount());
        model.addAttribute("citiesCovered", 25); // Static for now
        model.addAttribute("happyCustomers", 98); // Static for now
        
        return "index";
    }

    @GetMapping("/home")
    public String homePage() {
        return "redirect:/";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        return "auth/register";
    }
    
    @GetMapping("/register/step2")
    public String registerStep2(@RequestParam String type, Model model) {
        model.addAttribute("accountType", type);
        return "auth/register-step2";
    }
}
