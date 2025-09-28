package com.overlook.gestion.controller;

import com.overlook.gestion.domain.*;
import com.overlook.gestion.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Room Management
    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return adminService.getAllRooms();
    }

    @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = adminService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return adminService.updateRoom(id, room)
                .map(updatedRoom -> ResponseEntity.ok(updatedRoom))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (adminService.deleteRoom(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // User Management
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = adminService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return adminService.updateUser(id, user)
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (adminService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Reservation Management
    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return adminService.getAllReservations();
    }

    @PutMapping("/reservations/{id}/status")
    public ResponseEntity<Void> updateReservationStatus(@PathVariable Long id, @RequestParam String status) {
        if (adminService.updateReservationStatus(id, status)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Employee Shift Management
    @GetMapping("/shifts")
    public List<EmployeeShift> getAllShifts() {
        return adminService.getAllShifts();
    }

    @PostMapping("/shifts")
    public ResponseEntity<EmployeeShift> createShift(@RequestBody EmployeeShift shift) {
        EmployeeShift createdShift = adminService.createShift(shift);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdShift);
    }

    @PutMapping("/shifts/{id}")
    public ResponseEntity<EmployeeShift> updateShift(@PathVariable Long id, @RequestBody EmployeeShift shift) {
        return adminService.updateShift(id, shift)
                .map(updatedShift -> ResponseEntity.ok(updatedShift))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/shifts/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        if (adminService.deleteShift(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
