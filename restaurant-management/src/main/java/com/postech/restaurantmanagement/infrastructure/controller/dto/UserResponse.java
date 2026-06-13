package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.util.Set;

/**
 * HTTP Response payload transfer record representing a registered User.
 */
public record UserResponse(
    Long id,
    String name,
    String email,
    String phoneNumber,
    Set<String> roles
) {}