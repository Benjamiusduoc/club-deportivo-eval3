package com.club.ms_reportes.service;

import com.club.ms_reportes.client.CuotaClient;
import com.club.ms_reportes.client.ReservaClient;
import com.club.ms_reportes.client.SocioClient;
import com.club.ms_reportes.exception.RecursoNoEncontradoException;
import com.club.ms_reportes.model.dto.ReporteResponseDTO;
import com.club.ms_reportes.model.entity.Reporte;
import com.club.ms_reportes.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final SocioClient socioClient;
    private final CuotaClient cuotaClient;
    private final ReservaClient reservaClient;

    public ReporteService(ReporteRepository reporteRepository, SocioClient socioClient,
                          CuotaClient cuotaClient, ReservaClient reservaClient) {
        this.reporteRepository = reporteRepository;
        this.socioClient = socioClient;
        this.cuotaClient = cuotaClient;
        this.reservaClient = reservaClient;
    }

    public ReporteResponseDTO generarReporte(String tipo) {
        log.info("Generando reporte de tipo: {}", tipo);
        String datos;

        switch (tipo.toUpperCase()) {
            case "SOCIOS" -> {
                long activos = socioClient.contarSociosActivos();
                datos = "{\"sociosActivos\": " + activos + "}";
            }
            case "CUOTAS" -> {
                long pendientes = cuotaClient.contarCuotasPendientes();
                long vencidas = cuotaClient.contarCuotasVencidas();
                datos = "{\"cuotasPendientes\": " + pendientes + ", \"cuotasVencidas\": " + vencidas + "}";
            }
            case "RESERVAS" -> {
                long hoy = reservaClient.contarReservasHoy();
                datos = "{\"reservasHoy\": " + hoy + "}";
            }
            default ->
                throw new IllegalArgumentException("Tipo de reporte no soportado: " + tipo);
        }

        Reporte reporte = new Reporte();
        reporte.setTipo(tipo.toUpperCase());
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setDatos(datos);

        Reporte guardado = reporteRepository.save(reporte);
        log.info("Reporte generado con ID: {}", guardado.getId());
        return ReporteResponseDTO.fromEntity(guardado);
    }

    public List<ReporteResponseDTO> listarHistorial() {
        log.info("Listando historial de reportes");
        return reporteRepository.findAllByOrderByFechaGeneracionDesc().stream()
                .map(ReporteResponseDTO::fromEntity)
                .toList();
    }

    public ReporteResponseDTO buscarPorId(Long id) {
        log.info("Buscando reporte con ID: {}", id);
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reporte no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Reporte no encontrado con ID: " + id);
                });
        return ReporteResponseDTO.fromEntity(reporte);
    }
}
