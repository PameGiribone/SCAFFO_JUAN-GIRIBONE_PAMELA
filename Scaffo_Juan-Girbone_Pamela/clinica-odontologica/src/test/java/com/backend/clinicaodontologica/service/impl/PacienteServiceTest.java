package com.backend.clinicaodontologica.service.impl;


import com.backend.clinicaodontologica.dto.entrada.DomicilioEntradaDto;
import com.backend.clinicaodontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class PacienteServiceTest {

    @Autowired
    private PacienteService pacienteService;

    @Test
    @Order(1)
    void deberiaRegistrarseUnPacienteDeNombreJuan_yRetornarSuId() {
        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Juan", "Perez", 123456, LocalDate.of(2024, 3, 22), new DomicilioEntradaDto("Calle", 1234, "Localidad", "Provincia"));

        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);

        assertNotNull(pacienteSalidaDto);
    }
    @Test
    @Order(2)
    void deberiaEliminarseElPacienteConId1(){


        assertDoesNotThrow(() -> pacienteService.eliminarPaciente(1L));
    }

    @Test
    @Order(3)
    void deberiaDevolverUnaListaVaciaDePacientes(){
        List<PacienteSalidaDto> pacientes = pacienteService.listarPacientes();

        assertTrue(pacientes.isEmpty());
    }
}