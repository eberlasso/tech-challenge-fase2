package com.postech.restaurantmanagement.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionsTest {

    @Test
    void shouldCreateEntityValidationException() {
        EntityValidationException ex = new EntityValidationException("invalid");
        assertEquals("invalid", ex.getMessage());
        assertTrue(ex instanceof BusinessException);
    }

    @Test
    void shouldCreateResourceAlreadyExistsException() {
        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("exists");
        assertEquals("exists", ex.getMessage());
        assertTrue(ex instanceof BusinessException);
    }

    @Test
    void shouldCreateResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
        assertTrue(ex instanceof BusinessException);
    }
}
