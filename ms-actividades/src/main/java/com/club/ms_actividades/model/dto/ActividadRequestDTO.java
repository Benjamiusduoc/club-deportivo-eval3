package com.club.ms_actividades.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
    import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActividadRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Size(max = 100, message = "La descripción es muy larga")
    private String descripcion;

    @NotNull(message = "Debe indicar el cupo máximo de la actividad")
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    private Integer cupoMaximo;

    @NotNull(message = "Debe indicar el precio de la actividad")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
}
