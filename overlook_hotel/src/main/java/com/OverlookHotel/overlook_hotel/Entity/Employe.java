package com.OverlookHotel.overlook_hotel.Entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastName;
    private String name;
    private String email;
    private String phone;
    private String position; //Job
    private String password; // Nouveau champ mot de passe

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    // getters / setters
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @OneToMany(mappedBy = "employe")
    private List<Schedules> schedules;
}
