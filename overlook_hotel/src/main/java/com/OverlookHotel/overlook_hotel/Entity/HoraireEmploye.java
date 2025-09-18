package com.OverlookHotel.overlook_hotel.Entity;

import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
public class HoraireEmploye {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate date;
    private String shift;
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    private Employe employe;
}