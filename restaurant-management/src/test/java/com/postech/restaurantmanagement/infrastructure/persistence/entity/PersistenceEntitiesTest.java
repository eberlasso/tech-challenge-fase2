package com.postech.restaurantmanagement.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersistenceEntitiesTest {

    @Test
    void shouldUseNoArgsAndAllArgsAndAccessors() {
        UserEntity owner = new UserEntity(1L, "Owner", "owner@email.com", "pwd", "1199", 1L);

        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(2L);
        restaurant.setName("Bistro");
        restaurant.setAddress("Main");
        restaurant.setCuisineType("French");
        restaurant.setOperatingHours("10-22");
        restaurant.setOwner(owner);

        MenuItemEntity menu = new MenuItemEntity();
        menu.setId(3L);
        menu.setName("Pizza");
        menu.setDescription("Cheese");
        menu.setPrice(new BigDecimal("10.00"));
        menu.setAvailableOnlyInRestaurant(true);
        menu.setImagePath("/img");
        menu.setRestaurant(restaurant);

        assertEquals(1L, owner.getId());
        assertEquals("Bistro", restaurant.getName());
        assertEquals(3L, menu.getId());
        assertEquals("Pizza", menu.getName());
        assertNotNull(menu.getRestaurant());
    }
}

