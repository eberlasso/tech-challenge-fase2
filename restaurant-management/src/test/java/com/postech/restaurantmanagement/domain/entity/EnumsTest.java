package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumsTest {

    @Test
    void shouldExposeExpectedUserRoleValues() {
        assertEquals(UserRole.CLIENT, UserRole.valueOf("CLIENT"));
        assertEquals(UserRole.RESTAURANT_OWNER, UserRole.valueOf("RESTAURANT_OWNER"));
        assertEquals(UserRole.DELIVERY_DRIVER, UserRole.valueOf("DELIVERY_DRIVER"));
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
    }

    @Test
    void shouldExposeExpectedReservationStatusValues() {
        assertEquals(ReservationStatus.PENDING, ReservationStatus.valueOf("PENDING"));
        assertEquals(ReservationStatus.CONFIRMED, ReservationStatus.valueOf("CONFIRMED"));
        assertEquals(ReservationStatus.CANCELLED, ReservationStatus.valueOf("CANCELLED"));
        assertEquals(ReservationStatus.COMPLETED, ReservationStatus.valueOf("COMPLETED"));
    }
}

