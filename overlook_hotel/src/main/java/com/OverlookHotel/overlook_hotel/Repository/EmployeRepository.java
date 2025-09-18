package com.OverlookHotel.overlook_hotel.Repository;

import com.OverlookHotel.overlook_hotel.Entity.Employe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {    
    List<Employe> findByLastName(String last_name);
    List<Employe> findByPosition(String position);
}