package com.club.ms_cuotas.repository;

import com.club.ms_cuotas.model.entity.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByIdSocio(Long idSocio);

    List<Cuota> findByEstado(Cuota.EstadoCuota estado);

    Optional<Cuota> findByIdSocioAndPeriodo(Long idSocio, String periodo);
}
