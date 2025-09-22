package com.overlook.gestion.repository;

import com.overlook.gestion.domain.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Long> {
    Optional<Gestionnaire> findByEmail(String email); // для логина
}
