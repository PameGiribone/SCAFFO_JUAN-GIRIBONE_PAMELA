package com.backend.clinicaodontologica.service.impl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.backend.clinicaodontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.repository.TurnoRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TurnoServiceTest {
    @Autowired
    private TurnoService turnoService;

    @MockBean
    private TurnoRepository turnoRepository;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    public void deberiaRegistrarTurnoCorrectamente() throws BadRequestException {
        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto();
        turnoEntradaDto.setPacienteId(1L);
        turnoEntradaDto.setOdontologoId(1L);

        when(pacienteService.buscarPacientePorId(1L)).thenReturn(new PacienteSalidaDto());
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(new OdontologoSalidaDto());
        when(turnoRepository.save(any(Turno.class))).thenReturn(new Turno());

        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        assertNotNull(turnoSalidaDto);
    }

    @Test
    @Order(2)
    public void noDeberiaPermitirRegistrarTurnoConPacienteNoEncontrado() {
        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto();
        turnoEntradaDto.setPacienteId(1L);
        turnoEntradaDto.setOdontologoId(1L);

        when(pacienteService.buscarPacientePorId(1L)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> turnoService.registrarTurno(turnoEntradaDto));
    }

    @Test
    @Order(3)
    public void noDeberiaPermitirRegistrarTurnoConOdontologoNoEncontrado() {
        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto();
        turnoEntradaDto.setPacienteId(1L);
        turnoEntradaDto.setOdontologoId(1L);

        when(pacienteService.buscarPacientePorId(1L)).thenReturn(new PacienteSalidaDto());
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> turnoService.registrarTurno(turnoEntradaDto));
    }
}

