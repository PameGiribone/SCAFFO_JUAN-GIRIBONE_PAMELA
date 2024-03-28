package com.backend.clinicaodontologica.controller;

import com.backend.clinicaodontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private ITurnoService turnoService;

    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }


    @PostMapping("/registrar")
    public ResponseEntity<TurnoSalidaDto> registrarPaciente(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.registrarTurno(turnoEntradaDto), HttpStatus.CREATED);
    }


    @GetMapping()
    public ResponseEntity<?> listarTurnos() {
        return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoSalidaDto> buscarTurnoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<TurnoSalidaDto> actualizarTurno(@RequestBody @Valid TurnoEntradaDto turno, @PathVariable Long id) throws ResourceNotFoundException {
      return new ResponseEntity<>(turnoService.modificarTurno(turno, id), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarTurno(@RequestParam Long id) throws ResourceNotFoundException {
        turnoService.eliminarTurno(id);
        return new ResponseEntity<>("Turno eliminado correctamente", HttpStatus.NO_CONTENT);
    }


}
