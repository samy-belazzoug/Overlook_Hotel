package com.overlook.gestion.service;

import com.overlook.gestion.domain.EmployeeShift;
import com.overlook.gestion.domain.User;
import com.overlook.gestion.repository.EmployeeShiftRepository;
import com.overlook.gestion.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeShiftRepository employeeShiftRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeShiftRepository employeeShiftRepository,
                          UserRepository userRepository) {
        this.employeeShiftRepository = employeeShiftRepository;
        this.userRepository = userRepository;
    }

    public List<EmployeeShift> getShiftsByEmployee(Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employeeShiftRepository.findByEmploye(employee);
    }

    public List<EmployeeShift> getShiftsByDate(LocalDate date) {
        return employeeShiftRepository.findByDate(date);
    }
}
