package com.OverlookHotel.overlook_hotel.Entity;

import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.sql.Date;
import java.util.Collections;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HoraireEmploye {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Date date;
    private String shift;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employe employee;
}