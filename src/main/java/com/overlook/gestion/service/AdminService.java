package com.overlook.gestion.service;

import com.overlook.gestion.domain.*;
import com.overlook.gestion.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final EmployeeShiftRepository employeeShiftRepository;

    public AdminService(RoomRepository roomRepository,
                       UserRepository userRepository,
                       ReservationRepository reservationRepository,
                       EmployeeShiftRepository employeeShiftRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.employeeShiftRepository = employeeShiftRepository;
    }

    // Room Management
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> updateRoom(Long id, Room room) {
        return roomRepository.findById(id)
                .map(existingRoom -> {
                    existingRoom.setNumero(room.getNumero());
                    existingRoom.setType(room.getType());
                    existingRoom.setPrix(room.getPrix());
                    existingRoom.setEtat(room.getEtat());
                    return roomRepository.save(existingRoom);
                });
    }

    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // User Management
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setTelephone(user.getTelephone());
                    existingUser.setPointsFidelite(user.getPointsFidelite());
                    return userRepository.save(existingUser);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Reservation Management
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public boolean updateReservationStatus(Long id, String status) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatut(status);
                    reservationRepository.save(reservation);
                    return true;
                })
                .orElse(false);
    }

    // Employee Shift Management
    public List<EmployeeShift> getAllShifts() {
        return employeeShiftRepository.findAll();
    }

    public EmployeeShift createShift(EmployeeShift shift) {
        return employeeShiftRepository.save(shift);
    }

    public Optional<EmployeeShift> updateShift(Long id, EmployeeShift shift) {
        return employeeShiftRepository.findById(id)
                .map(existingShift -> {
                    existingShift.setDate(shift.getDate());
                    existingShift.setShift(shift.getShift());
                    existingShift.setEmploye(shift.getEmploye());
                    return employeeShiftRepository.save(existingShift);
                });
    }

    public boolean deleteShift(Long id) {
        if (employeeShiftRepository.existsById(id)) {
            employeeShiftRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
