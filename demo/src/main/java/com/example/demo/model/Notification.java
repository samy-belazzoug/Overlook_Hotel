package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String contenu;
    private LocalDateTime date;
    private Long destinataireId;
    private String destinataireType;
    private String type;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }
    public String getDestinataireType() { return destinataireType; }
    public void setDestinataireType(String destinataireType) { this.destinataireType = destinataireType; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
