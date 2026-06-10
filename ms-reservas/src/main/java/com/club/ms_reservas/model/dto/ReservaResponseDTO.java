package com.club.ms_reservas.model.dto;

import com.club.ms_reservas.model.entity.Reserva;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaResponseDTO {

    private Long id;
    private Long idSocio;
    private Long idActividad;
    private Long idInstructor;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private LocalDate fechaCreacion;

    public static ReservaResponseDTO fromEntity(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(reserva.getId());
        dto.setIdSocio(reserva.getIdSocio());
        dto.setIdActividad(reserva.getIdActividad());
        dto.setIdInstructor(reserva.getIdInstructor());
        dto.setFecha(reserva.getFecha());
        dto.setHoraInicio(reserva.getHoraInicio());
        dto.setHoraFin(reserva.getHoraFin());
        dto.setEstado(reserva.getEstado());
        dto.setFechaCreacion(reserva.getFechaCreacion());
        return dto;
    }
}
