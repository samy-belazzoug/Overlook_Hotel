package com.overlook.gestion.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "horaires_employes")
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employe_id", nullable = false)
    private User employe;

    private LocalDate date;
    private String shift; // matin, soir, nuit

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getEmploye() { return employe; }
    public void setEmploye(User employe) { this.employe = employe; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
}
