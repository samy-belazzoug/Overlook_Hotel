package com.example.demo.controller;

import com.example.demo.model.Gestionnaire;
import com.example.demo.repository.GestionnaireRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    private final GestionnaireRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public GestionnaireController(GestionnaireRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<Gestionnaire> create(@RequestBody Gestionnaire g) {
        // шифруем пароль перед сохранением
        g.setMotDePasse(passwordEncoder.encode(g.getMotDePasse()));
        Gestionnaire saved = repository.save(g);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ READ all
    @GetMapping
    public ResponseEntity<List<Gestionnaire>> list() {
        return ResponseEntity.ok(repository.findAll());
    }

    // ✅ READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Gestionnaire> getOne(@PathVariable Long id) {
        Optional<Gestionnaire> g = repository.findById(id);
        return g.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Gestionnaire> update(@PathVariable Long id, @RequestBody Gestionnaire updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNom(updated.getNom());
                    existing.setPrenom(updated.getPrenom());
                    existing.setEmail(updated.getEmail());
                    if (updated.getMotDePasse() != null && !updated.getMotDePasse().isEmpty()) {
                        existing.setMotDePasse(passwordEncoder.encode(updated.getMotDePasse()));
                    }
                    existing.setTelephone(updated.getTelephone());
                    existing.setRole(updated.getRole());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

