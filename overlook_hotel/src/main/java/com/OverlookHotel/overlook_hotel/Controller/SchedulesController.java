package com.OverlookHotel.overlook_hotel.Controller;

import com.OverlookHotel.overlook_hotel.Entity.Schedules;
import com.OverlookHotel.overlook_hotel.Entity.Employe;
import com.OverlookHotel.overlook_hotel.Service.SchedulesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class SchedulesController {

    private final SchedulesService scheduleService;

    public SchedulesController(SchedulesService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/my-schedules")
    public List<Schedules> getMySchedules(HttpSession session) {
        Integer employeeId = (Integer) session.getAttribute("userId");
        if(employeeId == null) throw new RuntimeException("Not logged in");
        return scheduleService.getSchedulesByEmployeeId(employeeId);
    }

    @PostMapping("/my-schedules")
    public Schedules addMySchedule(@RequestBody Schedules schedule, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null) throw new RuntimeException("Not logged in");
        Employe e = new Employe();
        e.setId(userId);
        schedule.setEmploye(e);
        return scheduleService.addSchedules(schedule);
    }

    @PutMapping("/my-schedules/{id}")
    public Schedules updateMySchedule(@PathVariable Integer id, @RequestBody Schedules schedule, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null) throw new RuntimeException("Not logged in");
        Schedules existing = scheduleService.getSchedulesById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if(!existing.getEmploye().getId().equals(userId)) throw new RuntimeException("Not authorized");
        existing.setDate(schedule.getDate());
        existing.setShift(schedule.getShift());
        return scheduleService.updateSchedules(id, existing);
    }

    @DeleteMapping("/my-schedules/{id}")
    public void deleteMySchedule(@PathVariable Integer id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null) throw new RuntimeException("Not logged in");
        Schedules existing = scheduleService.getSchedulesById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if(!existing.getEmploye().getId().equals(userId)) throw new RuntimeException("Not authorized");
        scheduleService.deleteSchedules(id);
    }
}
