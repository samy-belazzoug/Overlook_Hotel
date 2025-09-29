package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Reservation;
import com.example.demo.model.Role;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User currentUser;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ===== LOGIN =====
    @GetMapping("/login")
    public String loginPage() { 
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Email non trouvé");
            return "login";
        }

        User user = optionalUser.get();

        // Vérifie le mot de passe encodé avec BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Mot de passe incorrect");
            return "login";
        }

        // Si tout est OK, on sauvegarde l'utilisateur courant
        this.currentUser = user;

        // Vérifie si l'utilisateur a le rôle ADMIN ou GESTIONNAIRE
        boolean isManagerOrAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN") || r.getName().equals("GESTIONNAIRE"));

        if (isManagerOrAdmin) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/welcome";
        }
    }





    // ===== REGISTER =====
    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String telephone,
                           Model model) {

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Cet email est déjà utilisé !");
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setTelephone(telephone);

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("Role CLIENT introuvable"));
        newUser.setRoles(Collections.singleton(clientRole));

        userRepository.save(newUser);

        return "redirect:/login?registered=true";
    }

    // ===== WELCOME =====
    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("welcomeMessage", "Bienvenue " + currentUser.getUsername() + " !");
        return "welcome";
    }


    // ===== FEEDBACK =====
    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        if (currentUser == null) return "redirect:/login";
        return "feedback";
    }

    @GetMapping("/all-feedbacks")
    public String allFeedbacks(Model model) {
        if (currentUser == null) return "redirect:/login";
        List<Map<String, Object>> feedbacks = jdbcTemplate.queryForList(
                "SELECT * FROM feedbacks ORDER BY date DESC");
        model.addAttribute("feedbacks", feedbacks);
        return "all_feedbacks";
    }

    @PostMapping("/submit-feedback")
    public String submitFeedback(@RequestParam String commentaire,
                                 @RequestParam int note) {
        if (currentUser == null) return "redirect:/login";
        jdbcTemplate.update(
                "INSERT INTO feedbacks (commentaire, note, date, client_id) VALUES (?, ?, NOW(), ?)",
                commentaire, note, currentUser.getId());
        return "redirect:/welcome?feedbacks_submitted=true";
    }

    // ===== RESERVATIONS =====
    @GetMapping("/reservations")
    public String reservationsPage(Model model) {
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("rooms", roomRepository.findByStatus("available"));
        return "reservations";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam Long roomId,
                          @RequestParam String dateDebut,
                          @RequestParam String dateFin) {
        if (currentUser == null) return "redirect:/login";

        Room room = roomRepository.findById(roomId).orElseThrow();
        Reservation res = new Reservation();
        res.setChambre(room);
        res.setClient(currentUser);
        res.setDateDebut(LocalDate.parse(dateDebut));
        res.setDateFin(LocalDate.parse(dateFin));
        res.setStatut("active");

        reservationRepository.save(res);

        room.setStatus("occupied");
        roomRepository.save(room);

        return "redirect:/my-reservations";
    }

    // ===== MY RESERVATIONS =====
    @GetMapping("/my-reservations")
    public String myReservations(Model model) {
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("reservations", reservationRepository.findByClient(currentUser));
        return "my_reservations";
    }

    @PostMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam Long reservationId) {
        Reservation res = reservationRepository.findById(reservationId).orElseThrow();
        if (res.getClient().getId().equals(currentUser.getId())) {
            Room room = res.getChambre();
            room.setStatus("available");
            roomRepository.save(room);
            reservationRepository.delete(res);
        }
        return "redirect:/my-reservations";
    }

    @PostMapping("/update-reservation")
    public String updateReservation(@RequestParam Long reservationId,
                                    @RequestParam String dateDebut,
                                    @RequestParam String dateFin) {
        Reservation res = reservationRepository.findById(reservationId).orElseThrow();
        if (res.getClient().getId().equals(currentUser.getId())) {
            res.setDateDebut(LocalDate.parse(dateDebut));
            res.setDateFin(LocalDate.parse(dateFin));
            reservationRepository.save(res);
        }
        return "redirect:/my-reservations";
    }

    // ===== PROFIL =====
    @GetMapping("/profil")
    public String profilPage(Model model) {
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("user", currentUser);
        return "profil";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam String email,
                                @RequestParam String telephone,
                                @RequestParam(required = false) String password) {
        if (currentUser == null) return "redirect:/login";
        if (password != null && !password.isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(password));
        }
        currentUser.setEmail(email);
        currentUser.setTelephone(telephone);
        userRepository.save(currentUser);
        return "redirect:/profil";
    }
}
    