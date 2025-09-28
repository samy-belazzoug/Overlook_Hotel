package com.overlook.gestion.repository;

import com.overlook.gestion.domain.EmployeeShift;
import com.overlook.gestion.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, Long> {
    List<EmployeeShift> findByEmploye(User employe);
    List<EmployeeShift> findByDate(LocalDate date);
    List<EmployeeShift> findByShift(String shift);
}
