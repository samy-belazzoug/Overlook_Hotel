package com.OverlookHotel.overlook_hotel.Entity;

import java.util.List;
import java.util.Collections;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;

    @OneToMany(mappedBy = "employe")
    private List<HoraireEmploye> horaires;
}
