package com.club.ms_asistencia.model.dto;

import com.club.ms_asistencia.model.entity.Asistencia;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AsistenciaResponseDTO {

    private Long id;
    private Long idReserva;
    private Long idSocio;
    private LocalDate fecha;
    private Boolean presente;
    private String observacion;

    public static AsistenciaResponseDTO fromEntity(Asistencia asistencia) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setId(asistencia.getId());
        dto.setIdReserva(asistencia.getIdReserva());
        dto.setIdSocio(asistencia.getIdSocio());
        dto.setFecha(asistencia.getFecha());
        dto.setPresente(asistencia.getPresente());
        dto.setObservacion(asistencia.getObservacion());
        return dto;
    }
}
