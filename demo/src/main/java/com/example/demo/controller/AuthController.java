package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> {
                    this.currentUser = user;
                    return "redirect:/welcome";
                })
                .orElse("redirect:/login?error=true");
    }


    // ===== REGISTER =====
    @GetMapping("/register")
    public String registerPage() { return "register"; }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String telephone) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/register?error=email_exists";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setTelephone(telephone);
        userRepository.save(newUser);
        return "redirect:/login?registered=true";
    }
    // ===== WELCOME =====
    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        if (currentUser != null) {
            model.addAttribute("welcomeMessage", "Bienvenue " + currentUser.getUsername() + " !");
        }
        return "welcome"; // juste message et liens
    }
    // ====== FEEDBACK ======
    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        if (currentUser == null) return "redirect:/login";
        return "feedback"; // page avec formulaire de feedback

    }
    @GetMapping("/all-feedbacks")
    public String allFeedbacks(Model model) {
        String sql = "SELECT * FROM feedbacks ORDER BY date DESC";
        List<Map<String, Object>> feedbacks = jdbcTemplate.queryForList(sql);
        model.addAttribute("feedbacks", feedbacks);
        return "all_feedbacks"; // une nouvelle page
    }

    @PostMapping("/submit-feedback")
    public String submitFeedback(@RequestParam String commentaire,
                                 @RequestParam int note) {
        if (currentUser == null) return "redirect:/login";
        String sql = "INSERT INTO feedbacks (commentaire, note, date, client_id) VALUES (?, ?, NOW(), ?)";
        jdbcTemplate.update(sql, commentaire, note, currentUser.getId());

        return "redirect:/welcome?feedbacks_submitted=true";
    }



    // ===== RESERVATIONS =====
    @GetMapping("/reservations")
    public String reservationsPage(Model model) {
        if (currentUser == null) return "redirect:/login";
        model.addAttribute("rooms", roomRepository.findByStatus("available"));
        return "reservations"; // page avec formulaire de réservation
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
                                @RequestParam(required = false) String password,
                                Model model) {
        if (currentUser == null) return "redirect:/login";
        currentUser.setEmail(email);
        currentUser.setTelephone(telephone);
        if (password != null && !password.isBlank()) currentUser.setPassword(password);
        userRepository.save(currentUser);
        return "redirect:/profil";
    }
}
