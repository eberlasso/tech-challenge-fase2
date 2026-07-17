package com.postech.restaurantmanagement.infrastructure.controller.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtosTest {

    @Test
    void shouldInstantiateAllControllerDtos() {
        CreateUserRequest createUser = new CreateUserRequest("n", "e", "p", "ph", Set.of("CLIENT"));
        UserResponse userResponse = new UserResponse(1L, "n", "e", "ph", Set.of("CLIENT"));

        CreateRestaurantRequest createRestaurant = new CreateRestaurantRequest("r", "a", "c", "h", 1L);
        RestaurantResponse restaurantResponse = new RestaurantResponse(1L, "r", "a", "c", "h", 1L);

        CreateMenuItemRequest createMenu = new CreateMenuItemRequest("m", "d", BigDecimal.ONE, true, "/img", 1L);
        MenuItemResponse menuResponse = new MenuItemResponse(1L, "m", "d", BigDecimal.ONE, true, "/img", 1L);

        AuditLogResponse auditResponse = new AuditLogResponse("id", "action", "details", LocalDateTime.now(), "SYSTEM");
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "msg", LocalDateTime.now(), "/path");

        assertEquals("n", createUser.name());
        assertEquals(1L, userResponse.id());
        assertEquals("r", createRestaurant.name());
        assertEquals(1L, restaurantResponse.id());
        assertEquals("m", createMenu.name());
        assertEquals(1L, menuResponse.id());
        assertEquals("id", auditResponse.id());
        assertEquals(400, errorResponse.status());
    }
}

