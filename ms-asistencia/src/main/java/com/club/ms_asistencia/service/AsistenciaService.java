package com.club.ms_asistencia.service;

import com.club.ms_asistencia.client.ReservaClient;
import com.club.ms_asistencia.client.SocioClient;
import com.club.ms_asistencia.exception.RecursoNoEncontradoException;
import com.club.ms_asistencia.model.dto.AsistenciaRequestDTO;
import com.club.ms_asistencia.model.dto.AsistenciaResponseDTO;
import com.club.ms_asistencia.model.entity.Asistencia;
import com.club.ms_asistencia.repository.AsistenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository asistenciaRepository;
    private final ReservaClient reservaClient;
    private final SocioClient socioClient;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, ReservaClient reservaClient,
                             SocioClient socioClient) {
        this.asistenciaRepository = asistenciaRepository;
        this.reservaClient = reservaClient;
        this.socioClient = socioClient;
    }

    public AsistenciaResponseDTO crearAsistencia(AsistenciaRequestDTO dto) {
        log.info("Registrando asistencia para reserva {} y socio {}", dto.getIdReserva(), dto.getIdSocio());

        reservaClient.validarReservaExistente(dto.getIdReserva());
        socioClient.validarSocioActivo(dto.getIdSocio());

        Asistencia asistencia = new Asistencia();
        asistencia.setIdReserva(dto.getIdReserva());
        asistencia.setIdSocio(dto.getIdSocio());
        asistencia.setFecha(dto.getFecha());
        asistencia.setPresente(dto.getPresente());
        asistencia.setObservacion(dto.getObservacion());

        Asistencia guardada = asistenciaRepository.save(asistencia);
        log.info("Asistencia registrada exitosamente con ID: {}", guardada.getId());

        return AsistenciaResponseDTO.fromEntity(guardada);
    }

    public List<AsistenciaResponseDTO> listarTodas() {
        log.info("Listando todas las asistencias");
        return asistenciaRepository.findAll()
                .stream()
                .map(AsistenciaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AsistenciaResponseDTO buscarPorId(Long id) {
        log.info("Buscando asistencia con ID: {}", id);
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la asistencia con ID: {}", id);
                    return new RecursoNoEncontradoException("Asistencia no encontrada con el ID proporcionado.");
                });
        return AsistenciaResponseDTO.fromEntity(asistencia);
    }

    public List<AsistenciaResponseDTO> listarPorReserva(Long idReserva) {
        log.info("Listando asistencias de la reserva con ID: {}", idReserva);
        return asistenciaRepository.findByIdReserva(idReserva)
                .stream()
                .map(AsistenciaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> listarPorSocio(Long idSocio) {
        log.info("Listando asistencias del socio con ID: {}", idSocio);
        return asistenciaRepository.findByIdSocio(idSocio)
                .stream()
                .map(AsistenciaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> listarPorFecha(LocalDate fecha) {
        log.info("Listando asistencias para la fecha: {}", fecha);
        return asistenciaRepository.findByFecha(fecha)
                .stream()
                .map(AsistenciaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
