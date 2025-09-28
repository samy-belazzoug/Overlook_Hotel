package com.overlook.gestion.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "chambres")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;
    
    private String type;
    
    @Column(name = "prix")
    private BigDecimal prix;
    
    @Column(name = "etat")
    private String etat = "disponible"; // disponible, occupée, nettoyage

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}
