package com.backend.clinicaodontologica.controller;

import com.backend.clinicaodontologica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.service.impl.OdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }




    @PostMapping("/registrar")
    public ResponseEntity<OdontologoSalidaDto> registrarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologo) {
        OdontologoSalidaDto response = odontologoService.registrarOdontologo(odontologo);
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @PutMapping("actualizar/{id}")
    public ResponseEntity<OdontologoSalidaDto> modificarOdontologo(@Valid @RequestBody OdontologoEntradaDto odontologo, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(odontologoService.modificarOdontologo(odontologo, id), HttpStatus.OK);
    }



    @GetMapping("{id}")
    public ResponseEntity<OdontologoSalidaDto> buscarOdontologoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(odontologoService.buscarOdontologoPorId(id), HttpStatus.OK);
    }


    @GetMapping()

    public ResponseEntity<List<OdontologoSalidaDto>> listarOdontologos() {
        return new ResponseEntity<>(odontologoService.listarOdontologos(), HttpStatus.OK);
    }



    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarOdontologo(@RequestParam Long id) throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(id);
        return new ResponseEntity<>("Odontologo eliminado correctamente", HttpStatus.NO_CONTENT);
    }
}
