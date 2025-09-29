package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Reservation;
import com.overlook.gestion.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations-test")
public class ReservationsTestController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getReservation(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isPresent()) {
                result.put("reservation", reservation.get());
                result.put("status", "success");
            } else {
                result.put("status", "error");
                result.put("message", "Réservation non trouvée");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
