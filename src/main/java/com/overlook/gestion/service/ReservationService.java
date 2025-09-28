package com.overlook.gestion.service;

import com.overlook.gestion.domain.Reservation;
import com.overlook.gestion.domain.Room;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.ReservationRepository;
import com.overlook.gestion.repository.RoomRepository;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                            RoomRepository roomRepository,
                            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(Long clientId, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        // Проверяем, что клиент существует
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Проверяем, что комната существует
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Проверяем доступность комнаты
        if (!isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new RuntimeException("Room is not available for the selected dates");
        }

        // Создаем резервацию
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(room);
        reservation.setDateDebut(checkIn);
        reservation.setDateFin(checkOut);
        reservation.setStatut("active");

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByClient(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return reservationRepository.findByClient(client);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public boolean cancelReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatut("annulée");
            reservationRepository.save(reservation.get());
            return true;
        }
        return false;
    }

    private boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservations(roomId, checkIn, checkOut);
        return conflictingReservations.isEmpty();
    }
}
