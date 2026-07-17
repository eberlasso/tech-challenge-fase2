package com.postech.restaurantmanagement.infrastructure.config;

import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public ListUsersUseCase listUsersUseCase(UserGateway userGateway) {
        return new ListUsersUseCase(userGateway);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserGateway userGateway) {
        return new GetUserByIdUseCase(userGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserGateway userGateway, AuditLogGateway auditLogGateway) {
        return new UpdateUserUseCase(userGateway, auditLogGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway, AuditLogGateway auditLogGateway) {
        return new DeleteUserUseCase(userGateway, auditLogGateway);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public GetRestaurantByIdUseCase getRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new GetRestaurantByIdUseCase(restaurantGateway);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(
            RestaurantGateway restaurantGateway,
            AuditLogGateway auditLogGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway, auditLogGateway);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(
            RestaurantGateway restaurantGateway,
            AuditLogGateway auditLogGateway) {
        return new DeleteRestaurantUseCase(restaurantGateway, auditLogGateway);
    }

    @Bean
    public ListMenuItemsUseCase listMenuItemsUseCase(MenuItemGateway menuItemGateway) {
        return new ListMenuItemsUseCase(menuItemGateway);
    }

    @Bean
    public GetMenuItemByIdUseCase getMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        return new GetMenuItemByIdUseCase(menuItemGateway);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(
            MenuItemGateway menuItemGateway,
            RestaurantGateway restaurantGateway,
            AuditLogGateway auditLogGateway) {
        return new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway, auditLogGateway);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(
            MenuItemGateway menuItemGateway,
            AuditLogGateway auditLogGateway) {
        return new DeleteMenuItemUseCase(menuItemGateway, auditLogGateway);
    }
}