package com.club.ms_cuotas.model.dto;

import com.club.ms_cuotas.model.entity.Cuota;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CuotaResponseDTO {

    private Long id;
    private Long idSocio;
    private BigDecimal monto;
    private String periodo;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String estado;
    private String metodoPago;

    public static CuotaResponseDTO fromEntity(Cuota c) {
        CuotaResponseDTO dto = new CuotaResponseDTO();
        dto.id = c.getId();
        dto.idSocio = c.getIdSocio();
        dto.monto = c.getMonto();
        dto.periodo = c.getPeriodo();
        dto.fechaEmision = c.getFechaEmision();
        dto.fechaVencimiento = c.getFechaVencimiento();
        dto.fechaPago = c.getFechaPago();
        dto.estado = c.getEstado() != null ? c.getEstado().name() : null;
        dto.metodoPago = c.getMetodoPago() != null ? c.getMetodoPago().name() : null;
        return dto;
    }
}
