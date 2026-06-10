package com.club.ms_notificaciones.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del socio no puede ser nulo")
    private Long idSocio;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(max = 100, message = "El asunto debe tener máximo 100 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje debe tener máximo 500 caracteres")
    private String mensaje;
}
