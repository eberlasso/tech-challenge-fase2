package com.postech.restaurantmanagement.infrastructure.config;

import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.usecase.GetAuditLogsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;

/**
 * Architectural configuration layer acts as the unified factory bean orchestrator.
 * Declares domain use case instantiations keeping business logic strictly uncoupled from framework stereotypes.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway) {
        return new CreateUserUseCase(userGateway);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(
            RestaurantGateway restaurantGateway,
            AuditLogGateway auditLogGateway) { // Adicionado o parâmetro aqui
        return new CreateRestaurantUseCase(restaurantGateway, auditLogGateway);
    }

    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public GetAuditLogsUseCase getAuditLogsUseCase(AuditLogGateway auditLogGateway) {
        return new GetAuditLogsUseCase(auditLogGateway);
    }
}