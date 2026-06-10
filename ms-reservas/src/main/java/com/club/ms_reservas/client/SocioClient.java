package com.club.ms_reservas.client;

import com.club.ms_reservas.exception.RecursoNoEncontradoException;
import com.club.ms_reservas.exception.ReglaNegocioException;
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

    public void validarSocioActivo(Long idSocio) {
        try {
            Map<?, ?> socio = webClient.get()
                    .uri(url + "/api/socios/{id}", idSocio)
                    .retrieve().bodyToMono(Map.class).block();
            if (socio == null || Boolean.FALSE.equals(socio.get("activo")))
                throw new ReglaNegocioException("Socio no activo con id: " + idSocio);
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException("Socio no encontrado: " + idSocio);
        }
    }
}
