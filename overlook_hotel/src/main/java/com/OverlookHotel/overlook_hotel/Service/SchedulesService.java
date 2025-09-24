package com.OverlookHotel.overlook_hotel.Service;

import com.OverlookHotel.overlook_hotel.Entity.Schedules;
import com.OverlookHotel.overlook_hotel.Repository.SchedulesRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SchedulesService {
    
    //Object of repository
    private final SchedulesRepository scheduleRepository;


    public SchedulesService(SchedulesRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedules> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedules> getSchedulesById(Integer id) {
        return scheduleRepository.findById(id);
    }

    public Schedules addSchedules(Schedules schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedules updateSchedules(Integer id, Schedules updatedSchedules) {
    return scheduleRepository.findById(id)
            .map(schedule -> {
                schedule.setDate(updatedSchedules.getDate());
                schedule.setShift(updatedSchedules.getShift());
                return scheduleRepository.save(schedule); 
            })
            .orElseThrow(() -> new RuntimeException("Schedule not found with id : " + id));
    }

    public void deleteSchedules(Integer id) {
        scheduleRepository.deleteById(id);
    }

}
