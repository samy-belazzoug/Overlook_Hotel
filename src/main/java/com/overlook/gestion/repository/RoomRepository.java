package com.overlook.gestion.repository;

import com.overlook.gestion.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByEtat(String etat);
    List<Room> findByType(String type);
    
    @Query("SELECT r FROM Room r WHERE r.etat = 'disponible' AND r.id NOT IN " +
           "(SELECT res.chambre.id FROM Reservation res WHERE " +
           "res.statut = 'active' AND " +
           "((res.dateDebut <= :checkOut AND res.dateFin >= :checkIn)))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
