package com.OverlookHotel.overlook_hotel.Controller;

import com.OverlookHotel.overlook_hotel.Entity.Employe;
import com.OverlookHotel.overlook_hotel.Service.EmployeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<Employe> opt = employeService.getEmployeByEmail(email);
        if(opt.isPresent()) {
            Employe employe = opt.get();
            if(employe.getPassword().equals(password)) {
                session.setAttribute("userId", employe.getId());
                session.setAttribute("userName", employe.getName());
                session.setAttribute("role", employe.getPosition());
                return "redirect:/admin.html";
            }
        }
        return "redirect:/login.html?error=true";
    }

    @GetMapping("/admin.html")
    public String admin(HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return "redirect:/login.html";
        }
        return "admin";
    }

    @GetMapping("/session-info")
    public Map<String, String> sessionInfo(HttpSession session) {
        Map<String, String> info = new HashMap<>();
        Integer userId = (Integer) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        if(userId != null && userName != null) {
            info.put("userId", userId.toString());
            info.put("userName", userName);
        } else {
            info.put("userId", "0");
            info.put("userName", "Unknown");
        }
        return info;
    }
}
