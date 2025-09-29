package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.GestionnaireRepository;
import com.overlook.gestion.repository.UserRepository;
import com.overlook.gestion.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login-test")
public class LoginTestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            Map<String, Object> debug = new HashMap<>();
            debug.put("received_email", email);
            debug.put("received_password", password != null ? "***" : null);
            
            // Test authentication
            try {
                Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                debug.put("authentication", "SUCCESS");
            } catch (Exception e) {
                debug.put("authentication", "FAILED: " + e.getMessage());
                return ResponseEntity.badRequest().body(debug);
            }
            
            // Find user
            Optional<User> user = userRepository.findByEmail(email);
            String role = "CLIENT";
            
            if (user.isPresent()) {
                debug.put("user_found", true);
                debug.put("user_id", user.get().getId());
                debug.put("user_username", user.get().getUsername());
                role = user.get().getRoles().isEmpty() ? "CLIENT" : user.get().getRoles().iterator().next().getName();
                debug.put("user_role", role);
            } else {
                Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
                if (gestionnaire.isPresent()) {
                    debug.put("gestionnaire_found", true);
                    debug.put("gestionnaire_id", gestionnaire.get().getId());
                    role = gestionnaire.get().getRole();
                    debug.put("gestionnaire_role", role);
                } else {
                    debug.put("user_found", false);
                    debug.put("gestionnaire_found", false);
                }
            }
            
            // Generate token
            try {
                String token = jwtUtil.generateToken(email, role);
                debug.put("token_generated", true);
                debug.put("token_length", token.length());
                
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", role);
                response.put("message", "Login réussi");
                response.put("debug", debug.toString());
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                debug.put("token_generation", "FAILED: " + e.getMessage());
                return ResponseEntity.badRequest().body(debug);
            }
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
