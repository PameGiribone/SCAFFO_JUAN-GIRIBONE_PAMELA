package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaodontologica.dto.salida.PacienteSalidaDto;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.PacienteRepository;
import com.backend.clinicaodontologica.service.IPacienteService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {


    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);


    private PacienteRepository pacienteRepository;

    private ModelMapper modelMapper;


    public PacienteService(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {

        LOGGER.info("PacienteEntradaDto: {}", JsonPrinter.toString(paciente));

        Paciente pacienteEntidad = modelMapper.map(paciente, Paciente.class);

        Paciente pacienteEntidaConId = pacienteRepository.save(pacienteEntidad);

        PacienteSalidaDto pacienteSalidaDto = modelMapper.map(pacienteEntidaConId, PacienteSalidaDto.class);

        LOGGER.info("PacienteSalidaDto: {}", JsonPrinter.toString(pacienteSalidaDto));
        return  pacienteSalidaDto;
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {

        List<PacienteSalidaDto> pacientesSalidaDto = pacienteRepository.findAll()
                .stream()
                .map(paciente -> modelMapper.map(paciente, PacienteSalidaDto.class))
                .toList();

        LOGGER.info("Listado de todos los pacientes: {}", JsonPrinter.toString(pacientesSalidaDto));
        return pacientesSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(Long id) {

        Paciente pacienteBuscado = pacienteRepository.findById(id).orElse(null);
        PacienteSalidaDto pacienteEncontrado = null;

        if (pacienteBuscado != null) {
            pacienteEncontrado = modelMapper.map(pacienteBuscado, PacienteSalidaDto.class);
            LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(pacienteEncontrado));

        } else
            LOGGER.error("No se ha encontrado el paciente con id {}", id);
        return pacienteEncontrado;
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException {

        if (buscarPacientePorId(id) != null){
            pacienteRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el paciente con id {}", id);
        } else {

            throw new ResourceNotFoundException("No existe registro de paciente con id " + id);
        }

    }

    @Override

        public PacienteSalidaDto modificarPaciente(PacienteEntradaDto pacienteEntradaDto, Long id) throws ResourceNotFoundException {

            Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
            PacienteSalidaDto pacienteSalidaDto = null;
            if (optionalPaciente.isPresent()) {
                Paciente pacienteAActualizar = optionalPaciente.get();
                pacienteAActualizar.setNombre(pacienteEntradaDto.getNombre());
                pacienteAActualizar.setApellido(pacienteEntradaDto.getApellido());
                pacienteAActualizar.setDni(pacienteEntradaDto.getDni());
                pacienteAActualizar.setFechaIngreso(pacienteEntradaDto.getFechaIngreso());
                pacienteRepository.save(pacienteAActualizar);
                pacienteSalidaDto = modelMapper.map(pacienteAActualizar, PacienteSalidaDto.class);
                LOGGER.warn("Paciente actualizado: {}", JsonPrinter.toString(pacienteSalidaDto));
            } else {
                throw new ResourceNotFoundException("No existe paciente con id " + id);

            }
            return pacienteSalidaDto;
        }
    private void configureMapping() {
        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilioEntradaDto, Paciente::setDomicilio));

        modelMapper.typeMap(Paciente.class, PacienteSalidaDto.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDto::setDomicilioSalidaDto));

    }
}






