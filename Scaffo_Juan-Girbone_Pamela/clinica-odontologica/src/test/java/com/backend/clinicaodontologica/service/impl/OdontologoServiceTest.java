package com.backend.clinicaodontologica.service.impl;


import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)

    public void deberiaRegistrarOdontologoCorrectamente() {
        // Arrange
        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("123456", "Roberto", "Carlos");

        // Act
        OdontologoSalidaDto odontologoRegistrado = odontologoService.registrarOdontologo(odontologoEntradaDto);

        // Assert
        assertNotNull(odontologoRegistrado);
        assertNotNull(odontologoRegistrado.getId());
        assertEquals("Roberto", odontologoRegistrado.getNombre());
        assertEquals("Carlos", odontologoRegistrado.getApellido());
        assertEquals("123456", odontologoRegistrado.getMatricula());
    }

    @Test
    @Order(2)
    public void deberiaDevolverUnaListaConUnOdontologo() {
        // Arrange
        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("123456", "Roberto", "Carlos");
        OdontologoSalidaDto odontologoRegistrado = odontologoService.registrarOdontologo(odontologoEntradaDto);

        // Act
        List<OdontologoSalidaDto> odontologos = odontologoService.listarOdontologos();

        // Assert
        assertFalse(odontologos.isEmpty());
        assertEquals(1, odontologos.size());
        assertEquals(odontologoRegistrado.getId(), odontologos.get(0).getId());

    }


    @Test
    @Order(3)
    void deberiaEliminarseElOdontologoConId1(){


        assertDoesNotThrow(() -> odontologoService.eliminarOdontologo(1L));
    }
}
