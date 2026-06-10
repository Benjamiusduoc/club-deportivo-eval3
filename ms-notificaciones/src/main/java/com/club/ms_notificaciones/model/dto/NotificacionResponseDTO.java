package com.club.ms_notificaciones.model.dto;

import com.club.ms_notificaciones.model.entity.Notificacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {

    private Long id;
    private Long idSocio;
    private String tipo;
    private String asunto;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;

    public static NotificacionResponseDTO fromEntity(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(notificacion.getId());
        dto.setIdSocio(notificacion.getIdSocio());
        dto.setTipo(notificacion.getTipo());
        dto.setAsunto(notificacion.getAsunto());
        dto.setMensaje(notificacion.getMensaje());
        dto.setLeido(notificacion.getLeido());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        return dto;
    }
}
