package com.club.ms_reportes.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReporteSolicitudDTO {

    @NotBlank(message = "El tipo de reporte no puede estar vacío")
    private String tipo;
}
