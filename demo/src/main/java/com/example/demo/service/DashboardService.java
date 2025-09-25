package com.example.demo.service;

import com.example.demo.dto.DashboardDto;
import com.example.demo.repository.EvenementReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final EvenementReservationRepository reservationRepository;

    public DashboardService(EvenementReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public DashboardDto getDashboardStats() {
        long totalReservations = reservationRepository.count();
        long confirmed = reservationRepository.findAll().stream()
                .filter(r -> "CONFIRMED".equals(r.getStatut())).count();
        long cancelled = reservationRepository.findAll().stream()
                .filter(r -> "CANCELLED".equals(r.getStatut())).count();

        DashboardDto dto = new DashboardDto();
        dto.setConfirmedReservations(confirmed);
        dto.setCancelledReservations(cancelled);
        dto.setRevenus(0);       // TODO: revenus + moyenne notes (Dev1/Dev2 zones)
        dto.setTauxOccupation((confirmed * 1.0) / (totalReservations == 0 ? 1 : totalReservations));
        return dto;
    }
}
