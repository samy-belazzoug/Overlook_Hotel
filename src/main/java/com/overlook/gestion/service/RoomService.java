package com.overlook.gestion.service;

import com.overlook.gestion.domain.Room;
import com.overlook.gestion.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, 
                                          BigDecimal minPrice, BigDecimal maxPrice, String type) {
        if (checkIn != null && checkOut != null) {
            return roomRepository.findAvailableRooms(checkIn, checkOut);
        }
        
        List<Room> rooms = roomRepository.findAll();
        
        // Фильтрация по цене
        if (minPrice != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getPrix().compareTo(minPrice) >= 0)
                    .toList();
        }
        
        if (maxPrice != null) {
            rooms = rooms.stream()
                    .filter(room -> room.getPrix().compareTo(maxPrice) <= 0)
                    .toList();
        }
        
        // Фильтрация по типу
        if (type != null && !type.isEmpty()) {
            rooms = rooms.stream()
                    .filter(room -> room.getType().equalsIgnoreCase(type))
                    .toList();
        }
        
        return rooms;
    }
}
