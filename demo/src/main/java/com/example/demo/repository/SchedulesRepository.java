package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Schedules;

import java.time.LocalDate;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedules, Integer> {
    List<Schedules> findByDate(LocalDate date);
    List<Schedules> findByShift(String shift);
}