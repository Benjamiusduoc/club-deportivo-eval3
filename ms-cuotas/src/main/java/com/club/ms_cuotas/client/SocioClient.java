package com.club.ms_cuotas.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.club.ms_cuotas.exception.RecursoNoEncontradoException;
import com.club.ms_cuotas.exception.ReglaNegocioException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SocioClient {

    private final WebClient webClient;

    @Value("${ms.socios.url}")
    private String urlSocios;

    public void validarSocioActivo(Long idSocio) {
        try {
            Map<?, ?> socio = webClient.get()
                    .uri(urlSocios + "/api/socios/{id}")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (socio == null) {
                throw new RecursoNoEncontradoException(
                        "El socio con id " + idSocio + " no existe en ms-socios");
            }

            Object activo = socio.get("activo");
            if (activo == null || Boolean.FALSE.equals(activo)) {
                throw new ReglaNegocioException(
                        "El socio con id " + idSocio + " esta inactivo. No se pueden generar cuotas.");
            }
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException(
                    "El socio con id " + idSocio + " no existe en ms-socios");
        } catch (WebClientResponseException e) {
            throw new ReglaNegocioException(
                    "Error al consultar ms-socios: " + e.getStatusCode());
        }
    }
}
