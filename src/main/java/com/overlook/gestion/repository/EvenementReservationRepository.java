package com.overlook.gestion.repository;

import com.overlook.gestion.domain.EvenementReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvenementReservationRepository extends JpaRepository<EvenementReservation, Long> {
    List<EvenementReservation> findByEvenementId(Long evenementId);

    List<EvenementReservation> findByClientId(Long clientId);
    
    long countByEvenementId(Long evenementId);
}
