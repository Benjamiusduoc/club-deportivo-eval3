package com.club.ms_actividades.model.dto;

import com.club.ms_actividades.model.entity.Actividad;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActividadResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cupoMaximo;
    private BigDecimal precio;
    private Boolean activo;

    public static ActividadResponseDTO fromEntity(Actividad actividad) {
        ActividadResponseDTO dto = new ActividadResponseDTO();
        dto.setId(actividad.getId());
        dto.setNombre(actividad.getNombre());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setCupoMaximo(actividad.getCupoMaximo());
        dto.setPrecio(actividad.getPrecio());
        dto.setActivo(actividad.getActivo());
        return dto;
    }
}
