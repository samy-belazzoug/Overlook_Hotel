package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(String status); 
}
