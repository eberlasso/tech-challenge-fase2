package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.time.LocalDateTime;

/**
 * Standard API error payload used by global exception handling.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        String path
) {}

