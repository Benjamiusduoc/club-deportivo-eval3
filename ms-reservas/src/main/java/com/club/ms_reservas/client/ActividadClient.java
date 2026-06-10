package com.club.ms_reservas.client;

import com.club.ms_reservas.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ActividadClient {

    private final WebClient webClient;

    @Value("${ms.actividades.url}")
    private String url;

    public void validarActividadExistente(Long idActividad) {
        try {
            webClient.get().uri(url + "/api/actividades/{id}", idActividad)
                    .retrieve().bodyToMono(Map.class).block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException("Actividad no encontrada: " + idActividad);
        }
    }
}
