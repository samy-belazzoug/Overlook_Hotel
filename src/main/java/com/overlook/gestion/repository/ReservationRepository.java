package com.overlook.gestion.repository;

import com.overlook.gestion.domain.Reservation;
import com.overlook.gestion.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClient(User client);
    List<Reservation> findByStatut(String statut);
    
    @Query("SELECT r FROM Reservation r WHERE r.chambre.id = :roomId AND r.statut = 'active' AND " +
           "((r.dateDebut <= :checkOut AND r.dateFin >= :checkIn))")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId, 
                                                  @Param("checkIn") LocalDate checkIn, 
                                                  @Param("checkOut") LocalDate checkOut);
}
