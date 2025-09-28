package com.overlook.gestion.repository;

import com.overlook.gestion.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataireIdOrderByDateDesc(Long destinataireId);
}
