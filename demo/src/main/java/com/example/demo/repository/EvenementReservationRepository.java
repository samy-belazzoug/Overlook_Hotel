package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.EvenementReservation;

import java.util.List;

public interface EvenementReservationRepository extends JpaRepository<EvenementReservation, Long> {
    List<EvenementReservation> findByEvenementId(Long evenementId);

    List<EvenementReservation> findByClientId(Long clientId);
    
    long countByEvenementId(Long evenementId);
}
