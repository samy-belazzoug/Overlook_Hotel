package com.overlook.gestion.service;

import com.overlook.gestion.domain.Evenement;
import com.overlook.gestion.domain.EvenementReservation;
import com.overlook.gestion.dto.EvenementDto;

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
