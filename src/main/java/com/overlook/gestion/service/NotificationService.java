package com.overlook.gestion.service;

import com.overlook.gestion.domain.Notification;
import com.overlook.gestion.dto.NotificationDto;
import com.overlook.gestion.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> findByDestinataire(Long destinataireId) {
        return notificationRepository.findByDestinataireIdOrderByDateDesc(destinataireId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDto sendNotification(Long destinataireId, String destinataireType,
                                            String contenu, String type) {
        Notification n = new Notification();
        n.setDestinataireId(destinataireId);
        n.setDestinataireType(destinataireType);
        n.setContenu(contenu);
        n.setType(type);
        n.setDate(LocalDateTime.now());

        return mapToDto(notificationRepository.save(n));
    }

    private NotificationDto mapToDto(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setContenu(n.getContenu());
        dto.setDate(n.getDate());
        dto.setDestinataireId(n.getDestinataireId());
        dto.setType(n.getType());
        return dto;
    }
}





