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
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    public String createTestUser() {
        try {
            // Créer les rôles s'ils n'existent pas
            Role clientRole = roleRepository.findByName("CLIENT")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("CLIENT");
                    return roleRepository.save(role);
                });
            
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

            // Créer un utilisateur client
            User clientUser = new User();
            clientUser.setUsername("coucou");
            clientUser.setEmail("coucou@mail.com");
            clientUser.setPassword(passwordEncoder.encode("password123"));
            clientUser.setTelephone("0123456789");
            clientUser.setPointsFidelite(0);

            Set<Role> clientRoles = new HashSet<>();
            clientRoles.add(clientRole);
            clientUser.setRoles(clientRoles);
            userRepository.save(clientUser);
            
            // Créer un utilisateur admin
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@mail.com");
            adminUser.setPassword(passwordEncoder.encode("password123"));
            adminUser.setTelephone("0123456790");
            adminUser.setPointsFidelite(0);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRoles(adminRoles);
            userRepository.save(adminUser);
            
            return "Utilisateurs créés: coucou@mail.com (CLIENT) et admin@mail.com (ADMIN) / password123";
        } catch (Exception e) {
            return "Erreur lors de la création: " + e.getMessage();
        }
    }

    @GetMapping("/users")
    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
