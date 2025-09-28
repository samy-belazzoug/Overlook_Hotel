package com.overlook.gestion.controller;

import com.overlook.gestion.domain.EmployeeShift;
import com.overlook.gestion.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/shifts")
    public List<EmployeeShift> getMyShifts(@RequestParam Long employeeId) {
        return employeeService.getShiftsByEmployee(employeeId);
    }

    @GetMapping("/shifts/date")
    public List<EmployeeShift> getShiftsByDate(@RequestParam LocalDate date) {
        return employeeService.getShiftsByDate(date);
    }
}
