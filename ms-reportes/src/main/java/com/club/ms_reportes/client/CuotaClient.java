package com.club.ms_reportes.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CuotaClient {

    private final WebClient webClient;

    @Value("${ms.cuotas.url}")
    private String cuotasUrl;

    public long contarCuotasPendientes() {
        var list = webClient.get()
                .uri(cuotasUrl + "/api/cuotas/estado/PENDIENTE")
                .retrieve().bodyToMono(List.class).defaultIfEmpty(List.of()).block();
        return list.size();
    }

    public long contarCuotasVencidas() {
        var list = webClient.get()
                .uri(cuotasUrl + "/api/cuotas/estado/VENCIDA")
                .retrieve().bodyToMono(List.class).defaultIfEmpty(List.of()).block();
        return list.size();
    }
}
