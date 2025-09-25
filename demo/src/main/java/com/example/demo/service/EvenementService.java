package com.example.demo.service;

import com.example.demo.model.Evenement;
import com.example.demo.model.EvenementReservation;
import com.example.demo.dto.EvenementDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EvenementService {
    List<EvenementDto> listEvents(LocalDateTime from);

    Evenement createEvenement(EvenementDto dto, Long gestionnaireId);

    Optional<Evenement> updateEvenement(Long id, EvenementDto dto);

    boolean deleteEvenement(Long id);

    EvenementReservation reserveEvent(Long eventId, Long clientId);

    void cancelReservation(Long reservationId, Long clientId);
}
