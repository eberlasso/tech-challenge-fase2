package com.postech.restaurantmanagement.infrastructure.config;

import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetAuditLogsUseCase;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ConfigurationsTest {

    @Test
    void shouldCreateUseCaseBeansFromBeanConfiguration() {
        BeanConfiguration config = new BeanConfiguration();

        UserGateway userGateway = mock(UserGateway.class);
        RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
        MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
        AuditLogGateway auditLogGateway = mock(AuditLogGateway.class);

        CreateUserUseCase createUser = config.createUserUseCase(userGateway);
        CreateRestaurantUseCase createRestaurant = config.createRestaurantUseCase(restaurantGateway, auditLogGateway);
        CreateMenuItemUseCase createMenu = config.createMenuItemUseCase(menuItemGateway, restaurantGateway);
        GetAuditLogsUseCase getAudit = config.getAuditLogsUseCase(auditLogGateway);

        assertNotNull(createUser);
        assertNotNull(createRestaurant);
        assertNotNull(createMenu);
        assertNotNull(getAudit);
    }

    @Test
    void shouldBuildOpenApiMetadata() {
        OpenApiConfiguration config = new OpenApiConfiguration();
        OpenAPI openAPI = config.customOpenAPI();

        assertEquals("Restaurant Management API - FIAP Postech", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getContact());
    }
}

