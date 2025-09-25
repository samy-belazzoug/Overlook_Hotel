package com.example.demo.service;

import com.example.demo.repository.EmployeRepository;
import com.example.demo.model.Employe;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    public Optional<Employe> getEmployeById(Integer id) {
        return employeRepository.findById(id);
    }

    //CRUD OPERATIONS

    public Employe addEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe updateEmploye(Integer id, Employe updatedEmploye) {
        return employeRepository.findById(id)
                .map(employe -> {
                    employe.setLastName(updatedEmploye.getLastName());
                    employe.setName(updatedEmploye.getName());
                    employe.setEmail(updatedEmploye.getEmail());
                    employe.setPhone(updatedEmploye.getPhone());
                    employe.setPosition(updatedEmploye.getPosition());
                    return employeRepository.save(employe);
                })
                .orElseThrow(() -> new RuntimeException("Employe not found with id : " + id));
    }

    public void deleteEmploye(Integer id) {
        employeRepository.deleteById(id);
    }
}
