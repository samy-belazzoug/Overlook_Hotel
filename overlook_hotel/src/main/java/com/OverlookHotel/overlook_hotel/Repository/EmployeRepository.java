package com.OverlookHotel.overlook_hotel.Repository;

import com.OverlookHotel.overlook_hotel.Entity.Employe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface EmployeRepository extends JpaRepository<Employe, Integer> {    
    List<Employe> findByLastName(String lastName);
    List<Employe> findByPosition(String position);
    Optional<Employe> findByEmail(String email);

}