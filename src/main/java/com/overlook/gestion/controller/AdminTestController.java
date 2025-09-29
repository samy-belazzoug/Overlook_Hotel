package com.overlook.gestion.controller;

import com.overlook.gestion.domain.User;
import com.overlook.gestion.domain.Role;
import com.overlook.gestion.repository.UserRepository;
import com.overlook.gestion.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/admin-test")
public class AdminTestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create-admin")
    public String createAdmin() {
        try {
            // Créer le rôle ADMIN s'il n'existe pas
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

            // Vérifier si l'admin existe déjà
            var existingAdmin = userRepository.findByEmail("admin@mail.com");
            if (existingAdmin.isPresent()) {
                return "Admin existe déjà: admin@mail.com / password123";
            }

            // Créer un utilisateur admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@mail.com");
            adminUser.setPassword(passwordEncoder.encode("password123"));
            adminUser.setTelephone("0123456790");
            adminUser.setPointsFidelite(0);

            // Assigner le rôle ADMIN
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRoles(adminRoles);

            userRepository.save(adminUser);
            
            return "Admin créé avec succès: admin@mail.com / password123 (rôle: ADMIN)";
        } catch (Exception e) {
            return "Erreur lors de la création: " + e.getMessage();
        }
    }

    @GetMapping("/users")
    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
