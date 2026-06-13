package com.postech.restaurantmanagement.infrastructure.controller.dto;

import java.time.LocalDateTime;

/**
 * HTTP Response payload transfer record representing an immutable Audit trail log entry.
 */
public record AuditLogResponse(
        String id,
        String action,
        String details,
        LocalDateTime timestamp,
        String performedBy
) {}