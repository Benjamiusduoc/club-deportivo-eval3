package com.club.ms_inventario.model.dto;

import com.club.ms_inventario.model.entity.Item;
import lombok.Data;

@Data
public class ItemResponseDTO {

    private Long id;
    private String nombre;
    private Integer cantidadTotal;
    private Integer cantidadDisponible;
    private Long idActividad;
    private String estado;

    public static ItemResponseDTO fromEntity(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setNombre(item.getNombre());
        dto.setCantidadTotal(item.getCantidadTotal());
        dto.setCantidadDisponible(item.getCantidadDisponible());
        dto.setIdActividad(item.getIdActividad());
        dto.setEstado(item.getEstado());
        return dto;
    }
}
