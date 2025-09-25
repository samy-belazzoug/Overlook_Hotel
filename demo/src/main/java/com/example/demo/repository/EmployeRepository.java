package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Employe;

import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {    
    List<Employe> findByLastName(String lastName);
    List<Employe> findByPosition(String position);
}