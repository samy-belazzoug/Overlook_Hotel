package com.overlook.gestion.controller;

import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/users-test")
public class UsersTestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                result.put("user", user.get());
                result.put("status", "success");
            } else {
                result.put("status", "error");
                result.put("message", "Utilisateur non trouvé");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
