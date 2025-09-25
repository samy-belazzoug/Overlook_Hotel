package com.example.demo.controller;

import com.example.demo.model.Employe;
import com.example.demo.service.EmployeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }
    
    // GET : All employees
    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    // GET : Employe by id
    @GetMapping("/{id}")
    public Employe getEmployeById(@PathVariable Integer id) {
        return employeService.getEmployeById(id)
                .orElseThrow(() -> new RuntimeException("Employe not found with id : " + id));
    }

    // POST : Add an employe
    @PostMapping
    public Employe addEmploye(@RequestBody Employe employe) {
        return employeService.addEmploye(employe);
    }

    // PUT : Update an employe
    @PutMapping("/{id}")
    public Employe updateEmploye(@PathVariable Integer id, @RequestBody Employe updatedEmploye) {
        return employeService.updateEmploye(id, updatedEmploye);
    }

    // DELETE : Delete an employe
    @DeleteMapping("/{id}")
    public void deleteEmploye(@PathVariable Integer id) {
        employeService.deleteEmploye(id);
    }

    // GET : Search by last name
    @GetMapping("/search/last_name")
    public List<Employe> getByLastName(@RequestParam String lastName) {
        return employeService.getAllEmployes()
                .stream()
                .filter(h -> h.getLastName().equals(lastName))
                .toList();
    }

    // GET : Search by name
    @GetMapping("/search/name")
    public List<Employe> getByName(@RequestParam String name) {
        return employeService.getAllEmployes()
                .stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .toList();
    }

    // GET : Search by email
    @GetMapping("/search/email")
    public List<Employe> getByDate(@RequestParam String email) {
        return employeService.getAllEmployes()
                .stream()
                .filter(h -> h.getEmail().equals(email))
                .toList();
    }

    // GET : Search by phone
    @GetMapping("/search/phone")
    public List<Employe> getByShift(@RequestParam String phone) {
        return employeService.getAllEmployes()
                .stream()
                .filter(h -> h.getPhone().equalsIgnoreCase(phone))
                .toList();
    }

    // GET : Search by position
    @GetMapping("/search/position")
    public List<Employe> getByPosition(@RequestParam String position) {
        return employeService.getAllEmployes()
                .stream()
                .filter(h -> h.getPosition().equalsIgnoreCase(position))
                .toList();
    }

}
