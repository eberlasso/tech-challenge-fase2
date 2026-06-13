package com.postech.restaurantmanagement.infrastructure.controller.dto;

/**
 * HTTP Response payload transfer record representing a registered Restaurant.
 */
public record RestaurantResponse(
        Long id,
        String name,
        String address,
        String cuisineType,
        String operatingHours,
        Long ownerId
) {}