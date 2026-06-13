package com.postech.restaurantmanagement.domain.exception;

/**
 * Exception thrown when an enterprise entity fails its internal structural validation rules.
 */
public class EntityValidationException extends BusinessException {
    public EntityValidationException(String message) {
        super(message);
    }
}