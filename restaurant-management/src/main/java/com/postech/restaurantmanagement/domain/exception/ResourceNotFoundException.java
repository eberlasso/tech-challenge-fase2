package com.postech.restaurantmanagement.domain.exception;

/**
 * Exception thrown when a requested resource cannot be located.
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
