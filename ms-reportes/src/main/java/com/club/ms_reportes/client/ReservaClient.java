package com.club.ms_reportes.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ReservaClient {

    private final WebClient webClient;

    @Value("${ms.reservas.url}")
    private String reservasUrl;

    public long contarReservasHoy() {
        var list = webClient.get()
                .uri(reservasUrl + "/api/reservas/hoy")
                .retrieve().bodyToMono(List.class).defaultIfEmpty(List.of()).block();
        return list.size();
    }
}
