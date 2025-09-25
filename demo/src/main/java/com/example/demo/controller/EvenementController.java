package com.example.demo.controller;

import com.example.demo.model.Evenement;
import com.example.demo.model.EvenementReservation;
import com.example.demo.dto.EvenementDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.service.EvenementService;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {
  private final EvenementService service;

  public EvenementController(EvenementService service) {
    this.service = service;
  }

  @GetMapping
  public List<EvenementDto> list(@RequestParam(required=false) String from) {
    LocalDateTime f = (from==null) ? LocalDateTime.now() : LocalDateTime.parse(from);
    return service.listEvents(f);
  }

  @PostMapping
  public ResponseEntity<EvenementDto> create(@RequestBody @Valid EvenementDto dto) {
    // Simplified: assume GestionnaireId provided in DTO
    Evenement ev = service.createEvenement(dto, dto.getGestionnaireId());
    return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(ev));
  }

  @PostMapping("/{id}/reserve")
  public ResponseEntity<ReservationResponseDto> reserve(@PathVariable Long id, @RequestParam Long clientId) {
    EvenementReservation res = service.reserveEvent(id, clientId);
    return ResponseEntity.ok(mapToReserveDto(res));
  }

  @DeleteMapping("/reservations/{id}")
  public ResponseEntity<Void> cancelReservation(@PathVariable Long id){
    service.cancelReservation(id, null);
    return ResponseEntity.noContent().build();
  }


  // UPDATE événement
@PutMapping("/{id}")
public ResponseEntity<EvenementDto> update(@PathVariable Long id, @RequestBody EvenementDto dto) {
    return service.updateEvenement(id, dto)
            .map(updated -> ResponseEntity.ok(mapToDto(updated)))
            .orElseGet(() -> ResponseEntity.notFound().build());
}

// DELETE événement
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (service.deleteEvenement(id)) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
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

  private ReservationResponseDto mapToReserveDto(EvenementReservation r) {
    ReservationResponseDto dto = new ReservationResponseDto();
    dto.setReservationId(r.getId());
    dto.setEvenementId(r.getEvenement() != null ? r.getEvenement().getId() : null);
    dto.setClientId(r.getClientId());
    dto.setDateReservation(r.getDateReservation());
    dto.setStatut(r.getStatut());
    return dto;
  }
}
