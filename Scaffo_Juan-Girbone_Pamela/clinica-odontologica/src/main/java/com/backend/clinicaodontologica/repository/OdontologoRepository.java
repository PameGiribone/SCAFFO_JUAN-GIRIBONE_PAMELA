package com.backend.clinicaodontologica.repository;

import com.backend.clinicaodontologica.entity.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontologoRepository extends JpaRepository<Odontologo, Long> {

    Odontologo findByMatricula(int matricula);

    Odontologo findByMatriculaAndNombre(int matricula, String nombre);
}
