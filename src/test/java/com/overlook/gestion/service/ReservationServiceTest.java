package com.overlook.gestion.service;

import com.overlook.gestion.domain.*;
import com.overlook.gestion.repository.ReservationRepository;
import com.overlook.gestion.repository.RoomRepository;
import com.overlook.gestion.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, roomRepository, userRepository);
    }

    @Test
    public void testCreateReservation_Success() {
        // Given
        User client = new User();
        client.setId(1L);
        client.setEmail("test@example.com");

        Room room = new Room();
        room.setId(1L);
        room.setNumero("101");
        room.setEtat("disponible");

        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reservation result = reservationService.createReservation(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2));

        // Then
        assertNotNull(result);
        assertEquals(client, result.getClient());
        assertEquals(room, result.getChambre());
        assertEquals("active", result.getStatut());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_RoomNotAvailable() {
        // Given
        User client = new User();
        client.setId(1L);

        Room room = new Room();
        room.setId(1L);

        Reservation conflictingReservation = new Reservation();
        conflictingReservation.setStatut("active");

        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(reservationRepository.findConflictingReservations(any(), any(), any())).thenReturn(Collections.singletonList(conflictingReservation));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(2));
        });
    }
}
