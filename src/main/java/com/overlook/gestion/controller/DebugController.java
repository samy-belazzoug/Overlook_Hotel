package com.overlook.gestion.controller;

import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public Object getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/test-login")
    public Map<String, Object> testLogin(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            response.put("received_email", email);
            response.put("received_password", password != null ? "***" : null);
            
            // Chercher l'utilisateur
            var user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                response.put("user_found", true);
                response.put("user_id", user.get().getId());
                response.put("user_username", user.get().getUsername());
                response.put("user_roles", user.get().getRoles().size());
            } else {
                response.put("user_found", false);
            }
            
            response.put("status", "success");
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
