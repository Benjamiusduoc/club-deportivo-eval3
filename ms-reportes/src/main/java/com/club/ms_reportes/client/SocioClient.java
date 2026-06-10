package com.club.ms_reportes.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SocioClient {

    private final WebClient webClient;

    @Value("${ms.socios.url}")
    private String sociosUrl;

    public long contarSociosActivos() {
        return webClient.get()
                .uri(sociosUrl + "/api/socios/estadisticas/activos")
                .retrieve().bodyToMono(Long.class).defaultIfEmpty(0L).block();
    }
}
