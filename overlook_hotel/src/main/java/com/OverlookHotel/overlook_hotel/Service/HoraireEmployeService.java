package com.OverlookHotel.overlook_hotel.Service;

import com.OverlookHotel.overlook_hotel.Entity.HoraireEmploye;
import com.OverlookHotel.overlook_hotel.Repository.HoraireEmployeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HoraireEmployeService {
    
    private final HoraireEmployeRepository horaireEmployeRepository;

    public HoraireEmployeService(HoraireEmployeRepository horaireEmployeRepository) {
        this.horaireEmployeRepository = horaireEmployeRepository;
    }

    public List<HoraireEmploye> getAllHorairesEmploye() {
        return horaireEmployeRepository.findAll();
    }

    public Optional<HoraireEmploye> getHoraireEmployeById(Integer id) {
        return horaireEmployeRepository.findById(id);
    }

    public HoraireEmploye addHoraireEmploye(HoraireEmploye horaireEmploye) {
        return horaireEmployeRepository.save(horaireEmploye);
    }

    public HoraireEmploye updateHoraireEmploye(Integer id, HoraireEmploye updatedHoraireEmploye) {
    return horaireEmployeRepository.findById(id)
            .map(horaireEmploye -> {
                horaireEmploye.setDate(updatedHoraireEmploye.getDate());
                horaireEmploye.setShift(updatedHoraireEmploye.getShift());
                return horaireEmployeRepository.save(horaireEmploye); // important !
            })
            .orElseThrow(() -> new RuntimeException("Horaire Employé non trouvé avec id " + id));
    }

    public void deleteHoraireEmploye(Integer id) {
        horaireEmployeRepository.deleteById(id);
    }

}
