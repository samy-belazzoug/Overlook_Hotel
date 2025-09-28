package com.overlook.gestion.repository;

import com.overlook.gestion.domain.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByDateAfter(LocalDateTime date); // найти все будущие события
}
