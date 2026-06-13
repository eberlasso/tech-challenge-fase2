package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.math.BigDecimal;

/**
 * HTTP Request payload transfer record for Menu Item registration.
 */
public record CreateMenuItemRequest(
    String name,
    String description,
    BigDecimal price,
    boolean availableOnlyInRestaurant,
    String imagePath, // Path string configuration matching specification rules
    Long restaurantId
) {}