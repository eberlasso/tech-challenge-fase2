package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void shouldBeValidAndExposeFields() {
        MenuItem item = MenuItem.builder()
                .id(2L)
                .name("Pizza")
                .description("Cheese")
                .price(new BigDecimal("35.50"))
                .availableOnlyInRestaurant(true)
                .imagePath("/img/pizza.jpg")
                .restaurant(Restaurant.builder().id(1L).build())
                .build();

        assertEquals(2L, item.getId());
        assertEquals("Pizza", item.getName());
        assertEquals("Cheese", item.getDescription());
        assertEquals(new BigDecimal("35.50"), item.getPrice());
        assertTrue(item.isAvailableOnlyInRestaurant());
        assertEquals("/img/pizza.jpg", item.getImagePath());
        assertEquals(1L, item.getRestaurant().getId());
        assertTrue(item.isValid());
    }

    @Test
    void shouldBeInvalidWhenPriceIsNotPositiveOrRestaurantMissing() {
        MenuItem zeroPrice = MenuItem.builder()
                .name("Pizza")
                .description("Cheese")
                .price(BigDecimal.ZERO)
                .imagePath("/img/pizza.jpg")
                .restaurant(Restaurant.builder().id(1L).build())
                .build();

        MenuItem missingRestaurant = MenuItem.builder()
                .name("Pizza")
                .description("Cheese")
                .price(new BigDecimal("1.00"))
                .imagePath("/img/pizza.jpg")
                .build();

        assertFalse(zeroPrice.isValid());
        assertFalse(missingRestaurant.isValid());
    }
}

