package com.OverlookHotel.overlook_hotel.Repository;

import com.OverlookHotel.overlook_hotel.Entity.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SchedulesRepository extends JpaRepository<Schedules, Integer> {
    List<Schedules> findByDate(LocalDate date);
    List<Schedules> findByShift(String shift);
}