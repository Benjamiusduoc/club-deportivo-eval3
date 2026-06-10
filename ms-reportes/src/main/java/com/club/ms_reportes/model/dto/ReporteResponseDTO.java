package com.club.ms_reportes.model.dto;

import com.club.ms_reportes.model.entity.Reporte;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReporteResponseDTO {

    private Long id;
    private String tipo;
    private LocalDateTime fechaGeneracion;
    private String datos;

    public static ReporteResponseDTO fromEntity(Reporte reporte) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setId(reporte.getId());
        dto.setTipo(reporte.getTipo());
        dto.setFechaGeneracion(reporte.getFechaGeneracion());
        dto.setDatos(reporte.getDatos());
        return dto;
    }
}
