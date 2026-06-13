package com.postech.restaurantmanagement.domain.exception;

/**
 * Exception thrown when an operation attempts to duplicate a unique business resource constraint.
 */
public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}