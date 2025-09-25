package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Schedules {

    //Parameters

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate date;
    private String shift;
    
    //Getters / Setters

    public Integer getId() { return id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    //Foreign key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    private Employe employe;
}