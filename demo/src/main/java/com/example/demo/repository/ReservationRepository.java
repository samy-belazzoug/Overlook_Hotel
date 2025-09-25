package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Reservation;
import com.example.demo.model.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Toutes les réservations d'un utilisateur
    List<Reservation> findByClient(User client);

}


