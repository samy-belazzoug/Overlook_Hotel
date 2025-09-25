package com.example.demo.service.impl;

import com.example.demo.service.EvenementService;
import com.example.demo.service.NotificationService;
import com.example.demo.model.Evenement;
import com.example.demo.model.EvenementReservation;
import com.example.demo.model.Gestionnaire;
import com.example.demo.dto.EvenementDto;
import com.example.demo.repository.EvenementRepository;
import com.example.demo.repository.EvenementReservationRepository;
import com.example.demo.repository.GestionnaireRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvenementServiceImpl implements EvenementService {

    private final EvenementRepository evenementRepository;
    private final EvenementReservationRepository reservationRepository;
    private final GestionnaireRepository GestionnaireRepository;
    private final NotificationService notificationService;

    public EvenementServiceImpl(EvenementRepository evenementRepository,
                                EvenementReservationRepository reservationRepository,
                                GestionnaireRepository GestionnaireRepository,
                                NotificationService notificationService) {
        this.evenementRepository = evenementRepository;
        this.reservationRepository = reservationRepository;
        this.GestionnaireRepository = GestionnaireRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvenementDto> listEvents(LocalDateTime from) {
        return evenementRepository.findByDateAfter(from).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Evenement createEvenement(EvenementDto dto, Long GestionnaireId) {
        Gestionnaire g = GestionnaireRepository.findById(GestionnaireId)
                .orElseThrow(() -> new IllegalArgumentException("Gestionnaire introuvable: " + GestionnaireId));

        Evenement ev = new Evenement();
        ev.setTitre(dto.getTitre());
        ev.setDescription(dto.getDescription());
        ev.setDate(dto.getDate());
        ev.setCapacity(dto.getCapacity());
        ev.setGestionnaire(g);

        return evenementRepository.save(ev);
    }

    @Override
    @Transactional
    public Optional<Evenement> updateEvenement(Long id, EvenementDto dto) {
        return evenementRepository.findById(id).map(ev -> {
            ev.setTitre(dto.getTitre());
            ev.setDescription(dto.getDescription());
            ev.setDate(dto.getDate());
            ev.setCapacity(dto.getCapacity());
            return evenementRepository.save(ev);
        });
    }

    @Override
    @Transactional
    public boolean deleteEvenement(Long id) {
        if (evenementRepository.existsById(id)) {
            evenementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public EvenementReservation reserveEvent(Long eventId, Long clientId) {
        Evenement evenement = evenementRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        long reserved = reservationRepository.countByEvenementId(eventId);
        if (reserved >= evenement.getCapacity()) {
            throw new RuntimeException("Capacité maximale atteinte pour cet événement");
        }

        EvenementReservation res = new EvenementReservation();
        res.setEvenement(evenement);
        res.setClientId(clientId);
        res.setDateReservation(LocalDateTime.now());
        res.setStatut("CONFIRMED");

        EvenementReservation saved = reservationRepository.save(res);


        notificationService.sendNotification(
                clientId,
                "CLIENT",
                "Votre réservation pour l'événement '" + evenement.getTitre() + "' a été confirmée.",
                "RESERVATION_CONFIRMED"
        );

        return saved;
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long clientId) {
        EvenementReservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable: " + reservationId));

        res.setStatut("CANCELLED");
        reservationRepository.save(res);


        notificationService.sendNotification(
                clientId,
                "CLIENT",
                "Votre réservation pour l'événement '" + res.getEvenement().getTitre() + "' a été annulée.",
                "RESERVATION_CANCELLED"
        );
    }

    private EvenementDto mapToDto(Evenement e) {
        EvenementDto dto = new EvenementDto();
        dto.setId(e.getId());
        dto.setTitre(e.getTitre());
        dto.setDescription(e.getDescription());
        dto.setDate(e.getDate());
        dto.setCapacity(e.getCapacity());
        dto.setGestionnaireId(e.getGestionnaire() != null ? e.getGestionnaire().getId() : null);
        return dto;
    }
}
