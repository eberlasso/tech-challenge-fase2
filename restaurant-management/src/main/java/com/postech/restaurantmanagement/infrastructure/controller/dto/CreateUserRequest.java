package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.util.Set;

/**
 * HTTP Request payload transfer record for User registration.
 */
public record CreateUserRequest(
    String name,
    String email,
    String password,
    String phoneNumber,
    Set<String> roles
) {}