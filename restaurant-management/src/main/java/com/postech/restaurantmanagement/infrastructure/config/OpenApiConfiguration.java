package com.postech.restaurantmanagement.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure configuration class utilizing OpenAPI v3 specifications.
 * Automatically generates interactive documentation metadata for exposing REST endpoints.
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management API - FIAP Postech")
                        .version("1.0.0")
                        .description("Automated backend system for restaurant management, user classification, and menu catalog rules.")
                        .contact(new Contact()
                                .name("Software Architecture Team")
                                .email("support@postech.com")));
    }
}