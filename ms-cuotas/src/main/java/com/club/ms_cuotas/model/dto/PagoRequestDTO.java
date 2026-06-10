package com.club.ms_cuotas.model.dto;

import com.club.ms_cuotas.model.entity.Cuota;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @NotNull(message = "El metodo de pago es obligatorio")
    private Cuota.MetodoPago metodoPago;
}
