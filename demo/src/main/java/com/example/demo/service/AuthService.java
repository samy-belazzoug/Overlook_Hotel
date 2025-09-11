package com.example.demo.service;

public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String login(User user) {
        // Implement login logic
        return "Login successful";
    }

    public String register(User user) {
        userRepository.save(user);
        return "Registration successful";
    }
}
