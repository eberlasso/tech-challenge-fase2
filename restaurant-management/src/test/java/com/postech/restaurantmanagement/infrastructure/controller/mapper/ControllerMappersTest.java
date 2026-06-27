package com.postech.restaurantmanagement.infrastructure.controller.mapper;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.infrastructure.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ControllerMappersTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final RestaurantMapper restaurantMapper = Mappers.getMapper(RestaurantMapper.class);
    private final MenuItemMapper menuItemMapper = Mappers.getMapper(MenuItemMapper.class);
    private final AuditLogMapper auditLogMapper = Mappers.getMapper(AuditLogMapper.class);

    @Test
    void shouldMapUserRequestToDomainAndDomainToResponse() {
        CreateUserRequest request = new CreateUserRequest(
                "John", "john@email.com", "Secret123", "11999999999", Set.of("client", "admin"));

        User domain = userMapper.toDomain(request);
        assertEquals("John", domain.getName());
        assertEquals("john@email.com", domain.getEmail());
        assertEquals("Secret123", domain.getPassword());
        assertEquals("11999999999", domain.getPhoneNumber());
        assertTrue(domain.getRoles().contains(UserRole.CLIENT));
        assertTrue(domain.getRoles().contains(UserRole.ADMIN));

        User withId = User.builder()
                .id(1L)
                .name(domain.getName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .phoneNumber(domain.getPhoneNumber())
                .roles(domain.getRoles())
                .build();

        UserResponse response = userMapper.toResponse(withId);
        assertEquals(1L, response.id());
        assertTrue(response.roles().contains("CLIENT"));
        assertTrue(response.roles().contains("ADMIN"));
    }

    @Test
    void shouldMapRestaurantRequestAndResponse() {
        CreateRestaurantRequest request = new CreateRestaurantRequest("Bistro", "Main", "French", "10-22", 7L);

        Restaurant domain = restaurantMapper.toDomain(request);
        assertEquals("Bistro", domain.getName());
        assertEquals(7L, domain.getOwner().getId());

        Restaurant withId = Restaurant.builder()
                .id(9L)
                .name(domain.getName())
                .address(domain.getAddress())
                .cuisineType(domain.getCuisineType())
                .operatingHours(domain.getOperatingHours())
                .owner(domain.getOwner())
                .build();

        RestaurantResponse response = restaurantMapper.toResponse(withId);
        assertEquals(9L, response.id());
        assertEquals(7L, response.ownerId());
    }

    @Test
    void shouldMapMenuItemRequestAndResponse() {
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza", "Cheese", new BigDecimal("10.00"), true, "/img/pizza.jpg", 5L);

        MenuItem domain = menuItemMapper.toDomain(request);
        assertEquals("Pizza", domain.getName());
        assertEquals(5L, domain.getRestaurant().getId());

        MenuItem withId = MenuItem.builder()
                .id(11L)
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .availableOnlyInRestaurant(domain.isAvailableOnlyInRestaurant())
                .imagePath(domain.getImagePath())
                .restaurant(domain.getRestaurant())
                .build();

        MenuItemResponse response = menuItemMapper.toResponse(withId);
        assertEquals(11L, response.id());
        assertEquals(5L, response.restaurantId());

        MenuItem withoutRestaurant = MenuItem.builder()
                .id(12L)
                .name("No Restaurant")
                .description("D")
                .price(new BigDecimal("1.00"))
                .availableOnlyInRestaurant(false)
                .imagePath("/img")
                .build();
        MenuItemResponse responseWithoutRestaurant = menuItemMapper.toResponse(withoutRestaurant);
        assertNull(responseWithoutRestaurant.restaurantId());
    }

    @Test
    void shouldMapAuditLogToResponse() {
        LocalDateTime now = LocalDateTime.now();
        AuditLog log = new AuditLog("id", "ACTION", "details", now, "SYSTEM");

        AuditLogResponse response = auditLogMapper.toResponse(log);
        assertEquals("id", response.id());
        assertEquals("ACTION", response.action());
        assertEquals(now, response.timestamp());

        Restaurant noOwner = Restaurant.builder()
                .id(21L)
                .name("No Owner")
                .address("A")
                .cuisineType("C")
                .operatingHours("H")
                .build();
        RestaurantResponse noOwnerResponse = restaurantMapper.toResponse(noOwner);
        assertNull(noOwnerResponse.ownerId());
    }

    @Test
    void shouldReturnNullWhenMappingNullSources() {
        assertNull(userMapper.toResponse(null));
        assertNull(restaurantMapper.toDomain(null));
        assertNull(restaurantMapper.toResponse(null));
        assertNull(menuItemMapper.toDomain(null));
        assertNull(menuItemMapper.toResponse(null));
        assertNull(auditLogMapper.toResponse(null));
    }
}

