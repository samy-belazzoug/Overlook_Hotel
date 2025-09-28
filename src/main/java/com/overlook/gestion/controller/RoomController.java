package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Room;
import com.overlook.gestion.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.findAllRooms();
    }

    @GetMapping("/search")
    public List<Room> searchRooms(
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String type) {
        return roomService.searchAvailableRooms(checkIn, checkOut, minPrice, maxPrice, type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(room -> ResponseEntity.ok(room))
                .orElse(ResponseEntity.notFound().build());
    }
}
