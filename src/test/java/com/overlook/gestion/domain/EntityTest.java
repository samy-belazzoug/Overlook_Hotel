package com.overlook.gestion.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EntityTest {

    @Test
    public void testRoomEntity() {
        Room room = new Room();
        room.setNumero("101");
        room.setType("simple");
        room.setPrix(new BigDecimal("50.00"));
        room.setEtat("disponible");

        assertEquals("101", room.getNumero());
        assertEquals("simple", room.getType());
        assertEquals(new BigDecimal("50.00"), room.getPrix());
        assertEquals("disponible", room.getEtat());
    }

    @Test
    public void testReservationEntity() {
        User client = new User();
        client.setEmail("test@example.com");
        client.setUsername("testuser");

        Room room = new Room();
        room.setNumero("101");

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(room);
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(2));
        reservation.setStatut("active");

        assertEquals(client, reservation.getClient());
        assertEquals(room, reservation.getChambre());
        assertEquals("active", reservation.getStatut());
        assertNotNull(reservation.getDateCreation());
    }
}
