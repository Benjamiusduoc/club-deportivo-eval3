package com.club.ms_reservas.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaRequestDTO {

    @NotNull(message = "El id del socio es obligatorio")
    private Long idSocio;

    @NotNull(message = "El id de la actividad es obligatorio")
    private Long idActividad;

    private Long idInstructor;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
}
