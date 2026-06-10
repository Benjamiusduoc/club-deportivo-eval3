package com.club.ms_reservas.repository;

import com.club.ms_reservas.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByIdSocio(Long idSocio);

    List<Reserva> findByIdActividad(Long idActividad);

    List<Reserva> findByFecha(LocalDate fecha);

    List<Reserva> findByFechaBetween(LocalDate start, LocalDate end);

    List<Reserva> findByEstado(String estado);
}
