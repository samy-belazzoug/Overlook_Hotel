package com.example.demo.controller;

import com.example.demo.dto.NotificationDto;
import com.example.demo.service.NotificationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  public List<NotificationDto> myNotifications(@RequestParam Long userId) {
      return notificationService.findByDestinataire(userId);
  }
  
@PostMapping
public NotificationDto send(@RequestParam Long destinataireId,
                            @RequestParam String destinataireType,
                            @RequestParam String contenu,
                            @RequestParam(defaultValue = "INFO") String type) {
    return notificationService.sendNotification(destinataireId, destinataireType, contenu, type);
}

}
