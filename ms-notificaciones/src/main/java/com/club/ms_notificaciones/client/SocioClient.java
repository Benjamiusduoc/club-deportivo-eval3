package com.club.ms_notificaciones.client;

import com.club.ms_notificaciones.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SocioClient {

    private final WebClient webClient;

    @Value("${ms.socios.url}")
    private String url;

    public void validarSocioExiste(Long idSocio) {
        try {
            webClient.get().uri(url + "/api/socios/{id}", idSocio)
                    .retrieve().bodyToMono(Map.class).block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException("Socio no encontrado: " + idSocio);
        }
    }
}
