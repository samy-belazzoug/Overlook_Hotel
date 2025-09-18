package com.OverlookHotel.overlook_hotel.Controller;

import com.OverlookHotel.overlook_hotel.Entity.Employe;
import com.OverlookHotel.overlook_hotel.Service.EmployeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    @GetMapping("/{id}")
    public Employe getEmployeById(@PathVariable Integer id) {
        return employeService.getEmployeById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec id " + id));
    }

    @PostMapping
    public Employe addEmploye(@RequestBody Employe employe) {
        return employeService.addEmploye(employe);
    }

    @PutMapping("/{id}")
    public Employe updateEmploye(@PathVariable Integer id, @RequestBody Employe updatedEmploye) {
        return employeService.updateEmploye(id, updatedEmploye);
    }

    @DeleteMapping("/{id}")
    public void deleteEmploye(@PathVariable Integer id) {
        employeService.deleteEmploye(id);
    }
}
