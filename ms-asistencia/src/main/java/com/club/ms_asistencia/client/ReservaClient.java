package com.club.ms_asistencia.client;

import com.club.ms_asistencia.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReservaClient {

    private final WebClient webClient;

    @Value("${ms.reservas.url}")
    private String url;

    public void validarReservaExistente(Long idReserva) {
        try {
            webClient.get().uri(url + "/api/reservas/{id}", idReserva)
                    .retrieve().bodyToMono(Map.class).block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException("Reserva no encontrada: " + idReserva);
        }
    }
}
