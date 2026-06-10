package com.club.ms_reportes.repository;

import com.club.ms_reportes.model.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByTipoOrderByFechaGeneracionDesc(String tipo);

    List<Reporte> findAllByOrderByFechaGeneracionDesc();
}
