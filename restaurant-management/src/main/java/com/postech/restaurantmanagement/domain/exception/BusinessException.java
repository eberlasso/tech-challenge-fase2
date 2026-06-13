package com.postech.restaurantmanagement.domain.exception;

/**
 * Base abstract exception for all enterprise and application business rule violations.
 */
public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}