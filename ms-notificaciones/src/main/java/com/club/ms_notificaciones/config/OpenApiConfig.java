package com.club.ms_notificaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msNotificacionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API ms-notificaciones")
                        .description("Microservicio de notificaciones")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Club Deportivo")
                                .email("contacto@clubdeportivo.cl")));
    }
}
