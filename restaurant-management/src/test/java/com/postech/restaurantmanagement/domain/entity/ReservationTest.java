package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldDefaultStatusToPendingAndAllowStatusUpdate() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .numberOfPeople(4)
                .build();

        assertEquals(1L, reservation.getId());
        assertEquals(2L, reservation.getClient().getId());
        assertEquals(3L, reservation.getRestaurant().getId());
        assertEquals(4, reservation.getNumberOfPeople());
        assertNotNull(reservation.getReservationDateTime());
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        assertTrue(reservation.isValid());

        reservation.updateStatus(ReservationStatus.CONFIRMED);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldBeInvalidWhenDateIsPastOrPeopleIsZero() {
        Reservation pastDate = Reservation.builder()
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().minusHours(1))
                .numberOfPeople(4)
                .build();

        Reservation zeroPeople = Reservation.builder()
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().plusHours(1))
                .numberOfPeople(0)
                .build();

        Reservation missingClientId = Reservation.builder()
                .client(User.builder().build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().plusHours(1))
                .numberOfPeople(2)
                .build();

        Reservation missingRestaurantId = Reservation.builder()
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().build())
                .reservationDateTime(LocalDateTime.now().plusHours(1))
                .numberOfPeople(2)
                .build();

        assertFalse(pastDate.isValid());
        assertFalse(zeroPeople.isValid());
        assertFalse(missingClientId.isValid());
        assertFalse(missingRestaurantId.isValid());
    }

    @Test
    void shouldKeepExplicitStatusFromBuilder() {
        Reservation reservation = Reservation.builder()
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .numberOfPeople(2)
                .status(ReservationStatus.CANCELLED)
                .build();

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    void shouldThrowWhenUpdatingStatusWithNull() {
        Reservation reservation = Reservation.builder()
                .client(User.builder().id(2L).build())
                .restaurant(Restaurant.builder().id(3L).build())
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .numberOfPeople(2)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservation.updateStatus(null));

        assertEquals("Status cannot be null.", ex.getMessage());
    }
}

