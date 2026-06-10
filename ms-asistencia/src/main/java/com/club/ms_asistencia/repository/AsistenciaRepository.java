package com.club.ms_asistencia.repository;

import com.club.ms_asistencia.model.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByIdReserva(Long idReserva);

    List<Asistencia> findByIdSocio(Long idSocio);

    List<Asistencia> findByFecha(LocalDate fecha);
}
