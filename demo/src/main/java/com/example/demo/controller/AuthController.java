package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User currentUser; 

    // ===== LOGIN =====
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> {
                    this.currentUser = user; // enregistrer comme "connecté"
                    model.addAttribute("message", "Bienvenue " + user.getUsername() + " !");
                    model.addAttribute("rooms", roomRepository.findByStatus("available"));
                    return "welcome"; 
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Identifiants invalides");
                    return "login";
                });
    }

    // ===== REGISTER =====
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email déjà utilisé");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);

        // ⚙️ Assigner automatiquement le rôle "CLIENT" (id = 1)
        jdbcTemplate.update(
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, 1)",
            newUser.getId()
        );

        model.addAttribute("message", "Inscription réussie, connectez-vous !");
        return "login";
    }

    // ===== WELCOME + RESERVATION =====
    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        model.addAttribute("rooms", roomRepository.findByStatus("available"));
        return "welcome";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam Long roomId,
                          @RequestParam String dateDebut,
                          @RequestParam String dateFin,
                          Model model) {
        Room room = roomRepository.findById(roomId).orElseThrow();

        if (this.currentUser == null) {
            model.addAttribute("error", "Vous devez être connecté pour réserver !");
            return "login";
        }

        Reservation res = new Reservation();
        res.setChambre(room);
        res.setClient(currentUser);
        res.setDateDebut(LocalDate.parse(dateDebut));
        res.setDateFin(LocalDate.parse(dateFin));
        res.setStatut("active");

        reservationRepository.save(res);

        // Mettre à jour le statut de la chambre
        room.setStatus("occupied");
        roomRepository.save(room);

        model.addAttribute("message", "Réservation réussie !");
        model.addAttribute("rooms", roomRepository.findByStatus("available"));
        return "welcome";
    }
}
