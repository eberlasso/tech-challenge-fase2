package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.math.BigDecimal;

/**
 * HTTP Response payload transfer record representing a registered Menu Item.
 */
public record MenuItemResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    boolean availableOnlyInRestaurant,
    String imagePath,
    Long restaurantId
) {}