package com.overlook.gestion.controller;

import com.overlook.gestion.domain.Room;
import com.overlook.gestion.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    private RoomController roomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roomController = new RoomController(roomService);
    }

    @Test
    public void testGetAllRooms() {
        // Given
        Room room1 = new Room();
        room1.setId(1L);
        room1.setNumero("101");
        room1.setType("simple");
        room1.setPrix(new BigDecimal("50.00"));

        Room room2 = new Room();
        room2.setId(2L);
        room2.setNumero("102");
        room2.setType("double");
        room2.setPrix(new BigDecimal("80.00"));

        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomService.findAllRooms()).thenReturn(rooms);

        // When
        List<Room> result = roomController.getAllRooms();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("101", result.get(0).getNumero());
        assertEquals("102", result.get(1).getNumero());
    }
}
