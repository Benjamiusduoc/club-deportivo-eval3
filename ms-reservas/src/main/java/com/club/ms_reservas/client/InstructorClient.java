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
public class InstructorClient {

    private final WebClient webClient;

    @Value("${ms.instructores.url}")
    private String url;

    public void validarInstructorExistente(Long idInstructor) {
        try {
            webClient.get().uri(url + "/api/instructores/{id}", idInstructor)
                    .retrieve().bodyToMono(Map.class).block();
        } catch (WebClientResponseException.NotFound e) {
            throw new RecursoNoEncontradoException("Instructor no encontrado: " + idInstructor);
        }
    }
}
