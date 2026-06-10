package com.club.ms_notificaciones.service;

import com.club.ms_notificaciones.client.SocioClient;
import com.club.ms_notificaciones.exception.RecursoNoEncontradoException;
import com.club.ms_notificaciones.model.dto.NotificacionRequestDTO;
import com.club.ms_notificaciones.model.dto.NotificacionResponseDTO;
import com.club.ms_notificaciones.model.entity.Notificacion;
import com.club.ms_notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;
    private final SocioClient socioClient;

    public NotificacionService(NotificacionRepository notificacionRepository, SocioClient socioClient) {
        this.notificacionRepository = notificacionRepository;
        this.socioClient = socioClient;
    }

    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        log.info("Creando notificacion para socio ID: {}", dto.getIdSocio());
        socioClient.validarSocioExiste(dto.getIdSocio());

        Notificacion notificacion = new Notificacion();
        notificacion.setIdSocio(dto.getIdSocio());
        notificacion.setTipo(dto.getTipo());
        notificacion.setAsunto(dto.getAsunto());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setLeido(false);
        notificacion.setFechaCreacion(LocalDateTime.now());

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificacion creada con ID: {}", guardada.getId());
        return NotificacionResponseDTO.fromEntity(guardada);
    }

    public List<NotificacionResponseDTO> listarTodas() {
        log.info("Listando todas las notificaciones");
        return notificacionRepository.findAll().stream()
                .map(NotificacionResponseDTO::fromEntity)
                .toList();
    }

    public NotificacionResponseDTO buscarPorId(Long id) {
        log.info("Buscando notificacion con ID: {}", id);
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Notificacion no encontrada con ID: {}", id);
                    return new RecursoNoEncontradoException("Notificacion no encontrada con ID: " + id);
                });
        return NotificacionResponseDTO.fromEntity(notificacion);
    }

    public List<NotificacionResponseDTO> listarPorSocio(Long idSocio) {
        log.info("Listando notificaciones del socio ID: {}", idSocio);
        return notificacionRepository.findByIdSocio(idSocio).stream()
                .map(NotificacionResponseDTO::fromEntity)
                .toList();
    }

    public List<NotificacionResponseDTO> listarNoLeidasPorSocio(Long idSocio) {
        log.info("Listando notificaciones no leidas del socio ID: {}", idSocio);
        return notificacionRepository.findByIdSocioAndLeido(idSocio, false).stream()
                .map(NotificacionResponseDTO::fromEntity)
                .toList();
    }

    public NotificacionResponseDTO marcarComoLeida(Long id) {
        log.info("Marcando notificacion ID: {} como leida", id);
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Notificacion no encontrada con ID: {}", id);
                    return new RecursoNoEncontradoException("Notificacion no encontrada con ID: " + id);
                });
        notificacion.setLeido(true);
        Notificacion guardada = notificacionRepository.save(notificacion);
        return NotificacionResponseDTO.fromEntity(guardada);
    }

    public long contarNoLeidas(Long idSocio) {
        log.info("Contando notificaciones no leidas del socio ID: {}", idSocio);
        return notificacionRepository.countByIdSocioAndLeido(idSocio, false);
    }
}
