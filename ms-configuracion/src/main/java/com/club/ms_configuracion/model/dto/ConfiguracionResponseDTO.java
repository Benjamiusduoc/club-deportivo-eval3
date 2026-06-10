package com.club.ms_configuracion.model.dto;

import com.club.ms_configuracion.model.entity.Configuracion;
import lombok.Data;

@Data
public class ConfiguracionResponseDTO {

    private Long id;
    private String clave;
    private String valor;
    private String descripcion;

    public static ConfiguracionResponseDTO fromEntity(Configuracion configuracion) {
        ConfiguracionResponseDTO dto = new ConfiguracionResponseDTO();
        dto.setId(configuracion.getId());
        dto.setClave(configuracion.getClave());
        dto.setValor(configuracion.getValor());
        dto.setDescripcion(configuracion.getDescripcion());
        return dto;
    }
}
