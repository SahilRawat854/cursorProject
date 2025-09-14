package com.spingo.controller;

import com.spingo.entity.User;
import com.spingo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register/step2")
    public String registerStep2(@RequestParam String accountType,
                               @RequestParam String username,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String drivingLicense,
                               @RequestParam String address,
                               @RequestParam String city,
                               @RequestParam String state,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDrivingLicense(drivingLicense);
            user.setAddress(address);
            user.setCity(city);
            user.setState(state);
            user.setPassword(password);
            user.setAccountType(User.AccountType.valueOf(accountType.toUpperCase()));
            
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/step2?type=" + accountType;
        }
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        
        // Redirect based on user role
        switch (user.getAccountType()) {
            case INDIVIDUAL_OWNER:
            case BUSINESS_OWNER:
                return "redirect:/owner/dashboard";
            case DELIVERY_PARTNER:
                return "redirect:/delivery/dashboard";
            case CUSTOMER:
            default:
                return "redirect:/customer/dashboard";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
