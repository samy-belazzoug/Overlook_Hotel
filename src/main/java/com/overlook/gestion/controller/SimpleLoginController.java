package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.GestionnaireRepository;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/simple-login")
public class SimpleLoginController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/test")
    public ResponseEntity<?> simpleLogin(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            Map<String, Object> response = new HashMap<>();
            response.put("received_email", email);
            response.put("received_password", password != null ? "***" : null);
            
            // Check user in users table
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                User foundUser = user.get();
                response.put("user_found", true);
                response.put("user_id", foundUser.getId());
                response.put("user_username", foundUser.getUsername());
                response.put("user_roles_count", foundUser.getRoles().size());
                
                // Check password
                boolean passwordMatches = passwordEncoder.matches(password, foundUser.getPassword());
                response.put("password_matches", passwordMatches);
                
                if (passwordMatches) {
                    String role = foundUser.getRoles().isEmpty() ? "CLIENT" : foundUser.getRoles().iterator().next().getName();
                    response.put("login_success", true);
                    response.put("role", role);
                    response.put("message", "Login successful");
                } else {
                    response.put("login_success", false);
                    response.put("message", "Invalid password");
                }
            } else {
                // Check gestionnaire table
                Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
                if (gestionnaire.isPresent()) {
                    Gestionnaire foundGestionnaire = gestionnaire.get();
                    response.put("gestionnaire_found", true);
                    response.put("gestionnaire_id", foundGestionnaire.getId());
                    response.put("gestionnaire_nom", foundGestionnaire.getNom());
                    
                    // Check password
                    boolean passwordMatches = passwordEncoder.matches(password, foundGestionnaire.getMotDePasse());
                    response.put("password_matches", passwordMatches);
                    
                    if (passwordMatches) {
                        response.put("login_success", true);
                        response.put("role", foundGestionnaire.getRole());
                        response.put("message", "Login successful");
                    } else {
                        response.put("login_success", false);
                        response.put("message", "Invalid password");
                    }
                } else {
                    response.put("user_found", false);
                    response.put("gestionnaire_found", false);
                    response.put("login_success", false);
                    response.put("message", "User not found");
                }
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            error.put("stack_trace", e.getStackTrace()[0].toString());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
