package com.postech.restaurantmanagement.infrastructure.config;

import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteUserUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetAuditLogsUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetMenuItemByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetRestaurantByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetUserByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListMenuItemsUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListRestaurantsUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListUsersUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateUserUseCase;
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
        ListUsersUseCase listUsers = config.listUsersUseCase(userGateway);
        GetUserByIdUseCase getUserById = config.getUserByIdUseCase(userGateway);
        UpdateUserUseCase updateUser = config.updateUserUseCase(userGateway, auditLogGateway);
        DeleteUserUseCase deleteUser = config.deleteUserUseCase(userGateway, auditLogGateway);
        ListRestaurantsUseCase listRestaurants = config.listRestaurantsUseCase(restaurantGateway);
        GetRestaurantByIdUseCase getRestaurantById = config.getRestaurantByIdUseCase(restaurantGateway);
        UpdateRestaurantUseCase updateRestaurant = config.updateRestaurantUseCase(restaurantGateway, auditLogGateway);
        DeleteRestaurantUseCase deleteRestaurant = config.deleteRestaurantUseCase(restaurantGateway, auditLogGateway);
        ListMenuItemsUseCase listMenuItems = config.listMenuItemsUseCase(menuItemGateway);
        GetMenuItemByIdUseCase getMenuItemById = config.getMenuItemByIdUseCase(menuItemGateway);
        UpdateMenuItemUseCase updateMenuItem = config.updateMenuItemUseCase(menuItemGateway, restaurantGateway, auditLogGateway);
        DeleteMenuItemUseCase deleteMenuItem = config.deleteMenuItemUseCase(menuItemGateway, auditLogGateway);

        assertNotNull(createUser);
        assertNotNull(createRestaurant);
        assertNotNull(createMenu);
        assertNotNull(getAudit);
        assertNotNull(listUsers);
        assertNotNull(getUserById);
        assertNotNull(updateUser);
        assertNotNull(deleteUser);
        assertNotNull(listRestaurants);
        assertNotNull(getRestaurantById);
        assertNotNull(updateRestaurant);
        assertNotNull(deleteRestaurant);
        assertNotNull(listMenuItems);
        assertNotNull(getMenuItemById);
        assertNotNull(updateMenuItem);
        assertNotNull(deleteMenuItem);
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
