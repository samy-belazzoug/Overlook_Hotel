package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Room;
import com.overlook.gestion.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms-test")
public class RoomsTestController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getRoom(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                result.put("room", room.get());
                result.put("status", "success");
            } else {
                result.put("status", "error");
                result.put("message", "Chambre non trouvée");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> createRoom(@RequestBody Room room) {
        Map<String, Object> result = new HashMap<>();
        try {
            Room savedRoom = roomRepository.save(room);
            result.put("room", savedRoom);
            result.put("status", "success");
            result.put("message", "Chambre créée avec succès");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateRoom(@PathVariable Long id, @RequestBody Room roomData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Room> existingRoom = roomRepository.findById(id);
            if (existingRoom.isPresent()) {
                Room room = existingRoom.get();
                room.setNumero(roomData.getNumero());
                room.setType(roomData.getType());
                room.setPrix(roomData.getPrix());
                room.setEtat(roomData.getEtat());
                
                Room savedRoom = roomRepository.save(room);
                result.put("room", savedRoom);
                result.put("status", "success");
                result.put("message", "Chambre mise à jour avec succès");
            } else {
                result.put("status", "error");
                result.put("message", "Chambre non trouvée");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRoom(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                roomRepository.deleteById(id);
                result.put("status", "success");
                result.put("message", "Chambre supprimée avec succès");
            } else {
                result.put("status", "error");
                result.put("message", "Chambre non trouvée");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }
}
