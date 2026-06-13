package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

/**
 * Orchestrator Use Case managing the business rules and constraints for registering a new Restaurant.
 * Automatically triggers cross-cutting audit logging upon successful execution.
 */
public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final AuditLogGateway auditLogGateway; // Novo contrato injetado

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway, AuditLogGateway auditLogGateway) {
        this.restaurantGateway = restaurantGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public Restaurant execute(Restaurant restaurant) {
        // 1. Executa a persistência operacional no PostgreSQL
        Restaurant savedRestaurant = restaurantGateway.save(restaurant);

        // 2. Dispara o log assíncrono/documental de auditoria para o MongoDB
        String auditDetails = String.format("Restaurant '%s' successfully registered with ID %d under cuisine type '%s'.",
                savedRestaurant.getName(), savedRestaurant.getId(), savedRestaurant.getCuisineType());

        auditLogGateway.log(
                "RESTAURANT_CREATION",
                auditDetails,
                "SYSTEM_USER" // Em ambiente produtivo, aqui iria o login extraído do SecurityContext
        );

        return savedRestaurant;
    }
}