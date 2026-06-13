package com.postech.restaurantmanagement.infrastructure.controller.dto;

/**
 * HTTP Request payload transfer record for Restaurant registration.
 */
public record CreateRestaurantRequest(
        String name,
        String address,
        String cuisineType,
        String operatingHours,
        Long ownerId
) {}