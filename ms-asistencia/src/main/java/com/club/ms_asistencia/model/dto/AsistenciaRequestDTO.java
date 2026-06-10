package com.club.ms_asistencia.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AsistenciaRequestDTO {

    @NotNull(message = "El id de la reserva es obligatorio")
    private Long idReserva;

    @NotNull(message = "El id del socio es obligatorio")
    private Long idSocio;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El estado de presente es obligatorio")
    private Boolean presente;

    private String observacion;
}
