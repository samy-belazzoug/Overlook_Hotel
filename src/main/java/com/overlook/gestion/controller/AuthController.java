package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.GestionnaireRepository;
import com.overlook.gestion.repository.UserRepository;
import com.overlook.gestion.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GestionnaireRepository gestionnaireRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(GestionnaireRepository gestionnaireRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Gestionnaire g) {
        Optional<Gestionnaire> existing = gestionnaireRepository.findByEmail(g.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        g.setMotDePasse(passwordEncoder.encode(g.getMotDePasse()));
        Gestionnaire saved = gestionnaireRepository.save(g);
        return ResponseEntity.ok(saved);
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            // Trouver l'utilisateur pour obtenir le rôle
            Optional<User> user = userRepository.findByEmail(request.getEmail());
            if (user.isPresent()) {
                String role = user.get().getRoles().isEmpty() ? "CLIENT" : user.get().getRoles().iterator().next().getName();
                String token = jwtUtil.generateToken(request.getEmail(), role);
                
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", role);
                response.put("message", "Login réussi");
                
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email ou mot de passe incorrect");
        }
    }

    // Classe pour la requête de login
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
