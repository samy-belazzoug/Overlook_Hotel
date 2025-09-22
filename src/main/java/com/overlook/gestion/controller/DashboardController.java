package com.overlook.gestion.controller;

import com.overlook.gestion.dto.DashboardDto;
import com.overlook.gestion.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardDto getStats() {
        return service.getDashboardStats();
    }
}
