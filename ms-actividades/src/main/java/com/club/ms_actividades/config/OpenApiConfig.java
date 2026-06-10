package com.club.ms_actividades.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msActividadesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Actividades - Club Deportivo")
                        .description("Microservicio de gestion de actividades del Club Deportivo")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo Club Deportivo")
                                .email("contacto@clubdeportivo.cl")));
    }
}
