package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Gestionnaire;
import com.overlook.gestion.repository.GestionnaireRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GestionnaireRepository gestionnaireRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(GestionnaireRepository gestionnaireRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
    public ResponseEntity<?> login(@RequestBody Gestionnaire g) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(g.getEmail(), g.getMotDePasse())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok("Login réussi pour: " + g.getEmail());
    }
}
