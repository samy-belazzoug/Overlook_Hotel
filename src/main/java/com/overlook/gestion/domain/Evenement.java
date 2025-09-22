package com.overlook.gestion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="evenements")
public class Evenement {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @Column(columnDefinition = "text")
    private String description;
    private LocalDateTime date;
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name="gestionnaire_id")
    private Gestionnaire gestionnaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }
}
