package com.OverlookHotel.overlook_hotel.Repository;

import com.OverlookHotel.overlook_hotel.Entity.HoraireEmploye;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HoraireEmployeRepository extends JpaRepository<HoraireEmploye, Integer> {
    List<HoraireEmploye> findByDate(LocalDate date);
    List<HoraireEmploye> findByShift(String shift);
}