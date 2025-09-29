package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Room;
import com.overlook.gestion.domain.Reservation;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.RoomRepository;
import com.overlook.gestion.repository.ReservationRepository;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard-test")
public class DashboardTestController {

    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Statistiques des chambres
            List<Room> allRooms = roomRepository.findAll();
            long availableRooms = allRooms.stream()
                .filter(room -> "disponible".equals(room.getEtat()))
                .count();
            long occupiedRooms = allRooms.stream()
                .filter(room -> "occupee".equals(room.getEtat()))
                .count();
            
            // Statistiques des réservations
            List<Reservation> allReservations = reservationRepository.findAll();
            long activeReservations = allReservations.stream()
                .filter(reservation -> "active".equals(reservation.getStatut()))
                .count();
            
            // Statistiques des utilisateurs
            List<User> allUsers = userRepository.findAll();
            
            // Calcul du taux d'occupation
            double occupancyRate = allRooms.size() > 0 ? 
                (double) occupiedRooms / allRooms.size() * 100 : 0;
            
            stats.put("totalRooms", allRooms.size());
            stats.put("availableRooms", availableRooms);
            stats.put("occupiedRooms", occupiedRooms);
            stats.put("activeReservations", activeReservations);
            stats.put("totalUsers", allUsers.size());
            stats.put("occupancyRate", Math.round(occupancyRate));
            stats.put("status", "success");
            
        } catch (Exception e) {
            stats.put("status", "error");
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }

    @GetMapping("/recent-reservations")
    public Map<String, Object> getRecentReservations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            // Prendre les 5 dernières réservations
            List<Reservation> recentReservations = reservations.stream()
                .sorted((r1, r2) -> r2.getDateCreation().compareTo(r1.getDateCreation()))
                .limit(5)
                .toList();
            
            result.put("reservations", recentReservations);
            result.put("status", "success");
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/room-status")
    public Map<String, Object> getRoomStatus() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Room> rooms = roomRepository.findAll();
            
            Map<String, Long> statusCounts = new HashMap<>();
            statusCounts.put("disponible", rooms.stream()
                .filter(room -> "disponible".equals(room.getEtat()))
                .count());
            statusCounts.put("occupee", rooms.stream()
                .filter(room -> "occupee".equals(room.getEtat()))
                .count());
            statusCounts.put("nettoyage", rooms.stream()
                .filter(room -> "nettoyage".equals(room.getEtat()))
                .count());
            statusCounts.put("maintenance", rooms.stream()
                .filter(room -> "maintenance".equals(room.getEtat()))
                .count());
            
            result.put("statusCounts", statusCounts);
            result.put("status", "success");
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}
