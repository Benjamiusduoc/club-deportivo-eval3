package com.club.ms_actividades.repository;

import com.club.ms_actividades.model.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByActivoTrue();

    Optional<Actividad> findByNombre(String nombre);
}
