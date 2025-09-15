package com.OverlookHotel.overlook_hotel.Service;

import com.OverlookHotel.overlook_hotel.Entity.Employe;
import com.OverlookHotel.overlook_hotel.Repository.EmployeRepository;
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

    public Employe addEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe updateEmploye(Integer id, Employe updatedEmploye) {
        return employeRepository.findById(id)
                .map(employe -> {
                    employe.setNom(updatedEmploye.getNom());
                    employe.setPrenom(updatedEmploye.getPrenom());
                    employe.setEmail(updatedEmploye.getEmail());
                    employe.setTelephone(updatedEmploye.getTelephone());
                    employe.setPoste(updatedEmploye.getPoste());
                    return employeRepository.save(employe);
                })
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec id " + id));
    }

    public void deleteEmploye(Integer id) {
        employeRepository.deleteById(id);
    }
}
