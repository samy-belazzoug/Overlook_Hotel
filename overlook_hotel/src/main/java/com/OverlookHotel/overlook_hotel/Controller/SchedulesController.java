package com.OverlookHotel.overlook_hotel.Controller;

import com.OverlookHotel.overlook_hotel.Entity.Schedules;
import com.OverlookHotel.overlook_hotel.Service.SchedulesService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class SchedulesController {

    private final SchedulesService scheduleService;

    public SchedulesController(SchedulesService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // GET : All schedules
    @GetMapping
    public List<Schedules> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // GET : Schedule by ID
    @GetMapping("/{id}")
    public Schedules getSchedulesById(@PathVariable Integer id) {
        return scheduleService.getSchedulesById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id : " + id));
    }

    // POST : Add a schedule
    @PostMapping
    public Schedules addSchedules(@RequestBody Schedules schedule) {
        return scheduleService.addSchedules(schedule);
    }

    // PUT : Modify a schedule
    @PutMapping("/{id}")
    public Schedules updateSchedules(@PathVariable Integer id, @RequestBody Schedules updatedSchedule) {
        return scheduleService.updateSchedules(id, updatedSchedule);
    }

    // DELETE : Delete a schedule
    @DeleteMapping("/{id}")
    public void deleteSchedules(@PathVariable Integer id) {
        scheduleService.deleteSchedules(id);
    }

    // GET : Search by date
    @GetMapping("/search/date")
    public List<Schedules> getByDate(@RequestParam LocalDate date) {
        return scheduleService.getAllSchedules()
                .stream()
                .filter(h -> h.getDate().equals(date))
                .toList();
    }

    // GET : Search by shift
    @GetMapping("/search/shift")
    public List<Schedules> getByShift(@RequestParam String shift) {
        return scheduleService.getAllSchedules()
                .stream()
                .filter(h -> h.getShift().equalsIgnoreCase(shift))
                .toList();
    }
}
