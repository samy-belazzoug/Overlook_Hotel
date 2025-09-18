package com.OverlookHotel.overlook_hotel.Controller;

import com.OverlookHotel.overlook_hotel.Entity.HoraireEmploye;
import com.OverlookHotel.overlook_hotel.Service.HoraireEmployeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/horaireEmployes")
public class HoraireEmployeController {

    private final HoraireEmployeService horaireEmployeService;

    public HoraireEmployeController(HoraireEmployeService horaireEmployeService) {
        this.horaireEmployeService = horaireEmployeService;
    }

    // GET : tous les horaires
    @GetMapping
    public List<HoraireEmploye> getAllHoraireEmploye() {
        return horaireEmployeService.getAllHorairesEmploye();
    }

    // GET : un horaire par ID
    @GetMapping("/{id}")
    public HoraireEmploye getHoraireEmployeById(@PathVariable Integer id) {
        return horaireEmployeService.getHoraireEmployeById(id)
                .orElseThrow(() -> new RuntimeException("Horaire Employé non trouvé avec id " + id));
    }

    // POST : ajouter un horaire
    @PostMapping
    public HoraireEmploye addHoraireEmploye(@RequestBody HoraireEmploye horaireEmploye) {
        return horaireEmployeService.addHoraireEmploye(horaireEmploye);
    }

    // PUT : modifier un horaire
    @PutMapping("/{id}")
    public HoraireEmploye updateHoraireEmploye(@PathVariable Integer id, @RequestBody HoraireEmploye updatedHoraire) {
        return horaireEmployeService.updateHoraireEmploye(id, updatedHoraire);
    }

    // DELETE : supprimer un horaire
    @DeleteMapping("/{id}")
    public void deleteHoraireEmploye(@PathVariable Integer id) {
        horaireEmployeService.deleteHoraireEmploye(id);
    }

    // GET : chercher par date
    @GetMapping("/search/date")
    public List<HoraireEmploye> getByDate(@RequestParam LocalDate date) {
        return horaireEmployeService.getAllHorairesEmploye()
                .stream()
                .filter(h -> h.getDate().equals(date))
                .toList();
    }

    // GET : chercher par shift
    @GetMapping("/search/shift")
    public List<HoraireEmploye> getByShift(@RequestParam String shift) {
        return horaireEmployeService.getAllHorairesEmploye()
                .stream()
                .filter(h -> h.getShift().equalsIgnoreCase(shift))
                .toList();
    }
}
