package com.club.ms_reservas.service;

import com.club.ms_reservas.client.ActividadClient;
import com.club.ms_reservas.client.InstructorClient;
import com.club.ms_reservas.client.SocioClient;
import com.club.ms_reservas.exception.RecursoNoEncontradoException;
import com.club.ms_reservas.exception.ReglaNegocioException;
import com.club.ms_reservas.model.dto.ReservaRequestDTO;
import com.club.ms_reservas.model.dto.ReservaResponseDTO;
import com.club.ms_reservas.model.entity.Reserva;
import com.club.ms_reservas.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final SocioClient socioClient;
    private final ActividadClient actividadClient;
    private final InstructorClient instructorClient;

    public ReservaService(ReservaRepository reservaRepository, SocioClient socioClient,
                          ActividadClient actividadClient, InstructorClient instructorClient) {
        this.reservaRepository = reservaRepository;
        this.socioClient = socioClient;
        this.actividadClient = actividadClient;
        this.instructorClient = instructorClient;
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        log.info("Creando reserva para socio {} en actividad {}", dto.getIdSocio(), dto.getIdActividad());

        socioClient.validarSocioActivo(dto.getIdSocio());
        actividadClient.validarActividadExistente(dto.getIdActividad());

        if (dto.getIdInstructor() != null) {
            instructorClient.validarInstructorExistente(dto.getIdInstructor());
        }

        if (!dto.getHoraFin().isAfter(dto.getHoraInicio())) {
            log.error("La hora de fin debe ser posterior a la hora de inicio");
            throw new ReglaNegocioException("La hora de fin debe ser posterior a la hora de inicio");
        }

        Reserva reserva = new Reserva();
        reserva.setIdSocio(dto.getIdSocio());
        reserva.setIdActividad(dto.getIdActividad());
        reserva.setIdInstructor(dto.getIdInstructor());
        reserva.setFecha(dto.getFecha());
        reserva.setHoraInicio(dto.getHoraInicio());
        reserva.setHoraFin(dto.getHoraFin());
        reserva.setEstado("CONFIRMADA");
        reserva.setFechaCreacion(LocalDate.now());

        Reserva guardada = reservaRepository.save(reserva);
        log.info("Reserva creada exitosamente con ID: {}", guardada.getId());

        return ReservaResponseDTO.fromEntity(guardada);
    }

    public List<ReservaResponseDTO> listarTodas() {
        log.info("Listando todas las reservas");
        return reservaRepository.findAll()
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ReservaResponseDTO buscarPorId(Long id) {
        log.info("Buscando reserva con ID: {}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la reserva con ID: {}", id);
                    return new RecursoNoEncontradoException("Reserva no encontrada con el ID proporcionado.");
                });
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public List<ReservaResponseDTO> listarPorSocio(Long idSocio) {
        log.info("Listando reservas del socio con ID: {}", idSocio);
        return reservaRepository.findByIdSocio(idSocio)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReservaResponseDTO> listarPorFecha(LocalDate fecha) {
        log.info("Listando reservas para la fecha: {}", fecha);
        return reservaRepository.findByFecha(fecha)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ReservaResponseDTO cancelarReserva(Long id) {
        log.info("Cancelando reserva con ID: {}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la reserva con ID: {}", id);
                    return new RecursoNoEncontradoException("Reserva no encontrada con el ID proporcionado.");
                });
        reserva.setEstado("CANCELADA");
        Reserva cancelada = reservaRepository.save(reserva);
        log.info("Reserva con ID: {} cancelada exitosamente", id);
        return ReservaResponseDTO.fromEntity(cancelada);
    }
}
