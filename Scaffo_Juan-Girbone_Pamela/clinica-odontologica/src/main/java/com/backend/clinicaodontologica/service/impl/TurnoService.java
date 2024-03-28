package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.dto.salida.TurnoSalidaDto;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.TurnoRepository;
import com.backend.clinicaodontologica.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;

    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }


    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException{
        TurnoSalidaDto turnoSalidaDto;
        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";
        String ambosNulos = "El paciente y el odontologo no se encuentran en nuestra base de datos";

        if(paciente == null || odontologo == null){
            if(paciente == null && odontologo == null){
                LOGGER.error(ambosNulos);
                throw new BadRequestException(ambosNulos);
            } else if (paciente == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new BadRequestException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new BadRequestException(odontologoNoEnBdd);
            }

        } else {
            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = entidadADtoSalida(turnoNuevo, paciente, odontologo);
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }


        return turnoSalidaDto;
    }



    @Override
    public List<TurnoSalidaDto> listarTurnos() {
     List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
            .map(turno -> {
                PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
                OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId());
                return entidadADtoSalida(turno, paciente, odontologo);
            }).toList();

        LOGGER.info("Listado de todos los turnos: {}", turnos);
        return turnos;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoEncontrado = null;
        if(turnoBuscado != null){
            PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoBuscado.getPaciente().getId());
            OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoBuscado.getOdontologo().getId());
            turnoEncontrado = entidadADtoSalida(turnoBuscado, paciente, odontologo);
            LOGGER.info("Turno encontrado: {}", turnoEncontrado);
        } else {
            LOGGER.error("No se ha encontrado el turno con id {}", id);
        }
        return turnoEncontrado;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if(buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else {
            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }
    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoEntradaDto turnoEntradaDto, Long id) throws ResourceNotFoundException {
        Optional<Turno> optionalTurno = turnoRepository.findById(id);
        TurnoSalidaDto turnoSalidaDto = null;
        if(optionalTurno.isPresent()){
            Turno turnoAActualizar = optionalTurno.get();
            turnoAActualizar.setFechaYHora(turnoEntradaDto.getFechaYHora());

            turnoRepository.save(turnoAActualizar);
            PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoAActualizar.getPaciente().getId());
            OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoAActualizar.getOdontologo().getId());
            turnoSalidaDto = entidadADtoSalida(turnoAActualizar, paciente, odontologo);
            LOGGER.warn("Turno actualizado: {}", turnoSalidaDto);
        } else {
            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }


        return turnoSalidaDto;
    }

    private TurnoSalidaDto entidadADtoSalida(Turno turno, PacienteSalidaDto pacienteSalidaDto, OdontologoSalidaDto odontologoSalidaDto){
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteSalidaDto(pacienteSalidaDto);
        turnoSalidaDto.setOdontologoSalidaDto(odontologoSalidaDto);
        return turnoSalidaDto;
    }

}

































