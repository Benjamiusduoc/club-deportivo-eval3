package com.club.ms_configuracion.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfiguracionRequestDTO {

    @NotBlank(message = "La clave no puede estar vacía")
    @Size(max = 50, message = "La clave debe tener máximo 50 caracteres")
    private String clave;

    @NotBlank(message = "El valor no puede estar vacío")
    @Size(max = 255, message = "El valor debe tener máximo 255 caracteres")
    private String valor;

    @Size(max = 255, message = "La descripción debe tener máximo 255 caracteres")
    private String descripcion;
}
