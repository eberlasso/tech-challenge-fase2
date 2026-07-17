package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void shouldBeValidWithRequiredFieldsAndOwnerId() {
        User ownerProxy = User.builder().id(7L).build();

        Restaurant restaurant = Restaurant.builder()
                .id(10L)
                .name("Bistro")
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(ownerProxy)
                .build();

        assertEquals(10L, restaurant.getId());
        assertEquals("Bistro", restaurant.getName());
        assertEquals("Main St", restaurant.getAddress());
        assertEquals("French", restaurant.getCuisineType());
        assertEquals("10:00-22:00", restaurant.getOperatingHours());
        assertEquals(7L, restaurant.getOwner().getId());
        assertTrue(restaurant.isValid());
    }

    @Test
    void shouldBeInvalidWhenOwnerIdIsNullOrRequiredFieldsMissing() {
        Restaurant missingName = Restaurant.builder()
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(User.builder().id(1L).build())
                .build();

        Restaurant missingOwnerId = Restaurant.builder()
                .name("Bistro")
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(User.builder().build())
                .build();

        assertFalse(missingName.isValid());
        assertFalse(missingOwnerId.isValid());
    }
}

