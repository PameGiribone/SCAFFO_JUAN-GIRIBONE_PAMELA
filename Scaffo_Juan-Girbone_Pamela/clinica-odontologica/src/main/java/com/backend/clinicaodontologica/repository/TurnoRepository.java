package com.backend.clinicaodontologica.repository;

import com.backend.clinicaodontologica.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    Turno findByFechaYHora(String fechaYHora);

    Turno findByOdontologo( Long odontologo_id);



}
